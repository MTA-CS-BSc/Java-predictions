package engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import dtos.*;
import engine.exceptions.UUIDNotFoundException;
import engine.history.HistoryManager;
import engine.logs.EngineLoggers;
import engine.modules.RandomGenerator;
import engine.modules.Utils;
import engine.parsers.XmlParser;
import engine.prototypes.implemented.Coordinate;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDWorld;
import engine.simulation.SingleSimulation;
import engine.validators.PRDWorldValidators;
import helpers.modules.SingletonObjectMapper;
import helpers.modules.ThreadPoolManager;
import helpers.types.PropTypes;
import helpers.types.SimulationState;
import helpers.types.TypesUtils;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class EngineAPI {
    protected HistoryManager historyManager;
    protected ThreadPoolManager threadPoolManager;
    public EngineAPI() {
        historyManager = new HistoryManager();
        threadPoolManager = new ThreadPoolManager();
        configureLoggers();
    }
    private void configureLoggers() {
        EngineLoggers.SIMULATION_LOGGER.setLevel(Level.OFF);
        EngineLoggers.API_LOGGER.setLevel(Level.OFF);

        EngineLoggers.formatLogger(EngineLoggers.XML_ERRORS_LOGGER);
        EngineLoggers.formatLogger(EngineLoggers.SIMULATION_ERRORS_LOGGER);
    }
    public ResponseDTO isHistoryEmpty() {
        return new ResponseDTO(200, historyManager.isEmpty());
    }
    public ResponseDTO writeHistoryToFile(String filePath) {
        try {
            FileOutputStream f = new FileOutputStream(filePath);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(historyManager);
            o.close();
            f.close();
            EngineLoggers.API_LOGGER.info("History saved successfully.");
            return new ResponseDTO(200, true);
        } catch (Exception e) {
            EngineLoggers.API_LOGGER.info("Could not save history: " + e.getMessage());
            return new ResponseDTO(200, false);
        }
    }
    public ResponseDTO loadHistory(String filePath) {
        try {
            FileInputStream fi = new FileInputStream(filePath);
            ObjectInputStream oi = new ObjectInputStream(fi);
            historyManager = (HistoryManager) oi.readObject();
            return new ResponseDTO(200);
        }

        catch (Exception e) {
            EngineLoggers.API_LOGGER.info(e.getMessage());
            return new ResponseDTO(500, "Attempted to load history but no history file was found");
        }
    }
    public ResponseDTO loadXml(String xmlPath) throws JAXBException, FileNotFoundException {
        PRDWorld prdWorld = XmlParser.parseWorldXml(xmlPath);
        ResponseDTO validateWorldResponse = PRDWorldValidators.validateWorld(prdWorld);

        if (Objects.isNull(validateWorldResponse.getErrorDescription())) {
            setInitialXmlWorld(new World(prdWorld));
            threadPoolManager.setThreadsAmount(prdWorld.getPRDThreadCount());
        }

        return validateWorldResponse;
    }
    public ResponseDTO isXmlLoaded() {
        return new ResponseDTO(200, historyManager.isXmlLoaded());
    }
    public ResponseDTO createSimulation() {
        SingleSimulation sm = new SingleSimulation(getInitialWorld());

        sm.setStartWorldState(sm.getWorld());

        historyManager.addPastSimulation(sm);
        return new ResponseDTO(200, sm.getUUID());
    }
    public ResponseDTO getSimulationDetails() {
        if (!historyManager.isXmlLoaded())
            return new ResponseDTO(400, "", "No loaded XML");

        return new ResponseDTO(200, historyManager.getMockSimulationForDetails());
    }
    public ResponseDTO setEnvironmentVariable(String uuid, PropertyDTO prop, String val) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(400, "UUID", UUIDNotFoundException.class.getSimpleName());

        Property foundProp = Utils.findEnvironmentPropertyByName(historyManager.getPastSimulation(uuid).getWorld(), prop.getName());

        if (!Objects.isNull(foundProp)) {
            if (val == null) {
                // UI clear button reset
                foundProp.getValue().setRandomInitialize(true);
                foundProp.getValue().setInit(null);
                foundProp.getValue().setCurrentValue(null);
                return new ResponseDTO(200, String.format("UUID [%s]: Reset environment variable [%s]",
                        uuid, foundProp.getName()));
            }

            if (!Utils.validateValueInRange(prop, val))
                return new ResponseDTO(500, String.format("Environment variable [%s] was not set", prop.getName()), "Value not in range");

            else if (!TypesUtils.validateType(prop, val))
                return new ResponseDTO(500, String.format("Environment variable [%s] was not set", prop.getName()), "Incorrect type");

            if (PropTypes.NUMERIC_PROPS.contains(foundProp.getType()))
                val = TypesUtils.removeExtraZeroes(val);

            foundProp.getValue().setRandomInitialize(false);
            foundProp.getValue().setInit(val);
            foundProp.getValue().setCurrentValue(foundProp.getValue().getInit());

            return new ResponseDTO(200, String.format("UUID [%s]: Set environment variable [%s]: Value: [%s]",
                    uuid, foundProp.getName(), foundProp.getValue().getCurrentValue()));
        }

        return new ResponseDTO(500, String.format("Environment variable [%s] was not set", prop.getName()),
                String.format("UUID [%s]: Environment variable [%s] not found", uuid, prop.getName()));
    }
    public ResponseDTO getEnvironmentProperties(String uuid) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(500, Collections.emptyList(), String.format("UUID [%s] not found", uuid));

        List<PropertyDTO> data = historyManager.getPastSimulation(uuid).getWorld().getEnvironment()
                .getEnvVars().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(PropertyDTO::getName))
                .collect(Collectors.toList());

        return new ResponseDTO(200, data);
    }
    public ResponseDTO getPastSimulations() throws JsonProcessingException {
        if (SingletonObjectMapper.objectMapper.readValue(isHistoryEmpty().getData(), Boolean.class))
            return new ResponseDTO(400, Collections.emptyList(), "History is empty!");

        List<SingleSimulationDTO> data = historyManager.getPastSimulations().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(SingleSimulationDTO::getStartTimestamp))
                .collect(Collectors.toList());

        return new ResponseDTO(200, data);
    }
    public ResponseDTO findSelectedSimulationDTO(int selection) throws Exception {
        List<SingleSimulationDTO> pastSimulations = SingletonObjectMapper.objectMapper.readValue(getPastSimulations().getData(),
                new TypeReference<List<SingleSimulationDTO>>() {});
        return new ResponseDTO(200, pastSimulations.get(selection - 1));
    }
    public ResponseDTO findSelectedEntityDTO(String uuid, int selection) throws JsonProcessingException {
        EntityDTO data = null;

        if (!Objects.isNull(findSimulationDTOByUuid(uuid))) {
            List<EntityDTO> entities = SingletonObjectMapper.objectMapper.readValue(getEntities(uuid).getData(), new TypeReference<List<EntityDTO>>(){});
            data = entities.get(selection - 1);
        }

        return Objects.isNull(data) ?
                new ResponseDTO(500, null, "Unknown") : new ResponseDTO(200, data);
    }
    public ResponseDTO findSelectedPropertyDTO(EntityDTO entity, int selection) {
        if (Objects.isNull(entity))
            return new ResponseDTO(500, null, "Entity is null");

        return new ResponseDTO(200, entity.getProperties().get(selection - 1));
    }
    public ResponseDTO getEntities(String uuid) throws JsonProcessingException {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(400, Collections.emptyList(), String.format("UUID [%s] not found", uuid));

        List<EntityDTO> data = historyManager.getPastSimulation(uuid)
                .getStartWorldState()
                .getEntitiesMap()
                .values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(EntityDTO::getName))
                .collect(Collectors.toList());

        return new ResponseDTO(200, data);
    }
    public ResponseDTO getEntitiesBeforeAndAfterSimulation(String uuid) throws UUIDNotFoundException {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(500, Collections.emptyMap(), String.format("UUID [%s] not found", uuid));

        return new ResponseDTO(200, historyManager.getEntitiesBeforeAndAfter(uuid));
    }
    public ResponseDTO getEntitiesCountForProp(String uuid, String entityName, String propertyName) throws UUIDNotFoundException, JsonProcessingException {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(500, Collections.emptyMap(), String.format("UUID [%s] not found", uuid));

        return new ResponseDTO(200, historyManager.getEntitiesCountForProp(uuid, entityName, propertyName));
    }
    public ResponseDTO runSimulation(String uuid) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(500, String.format("Simulation [%s] was not executed", uuid), String.format("Simulation [%s] could not be found", uuid));

        //TODO: Check if catch should be ignored
        threadPoolManager.executeTask(() -> {
            try {
                startSimulation(uuid);
            } catch (Exception ignored) { }
        });

        return new ResponseDTO(200, String.format("Simulation [%s] was added to thread pool", uuid));
    }
    public ResponseDTO getCurrentOverallPopulation(String uuid) {
        int sum = historyManager.getPastSimulation(uuid)
                .getWorld()
                .getEntities()
                .getEntitiesMap()
                .values()
                .stream()
                .mapToInt(Entity::getPopulation)
                .sum();

        return new ResponseDTO(200, sum);
    }
    public ResponseDTO setEntityInitialPopulation(String uuid, String entityName, int population) {
        if (population < 0)
            return new ResponseDTO(400, String.format("Simulation [%s]: Entity [%s]: Population has not been initialized",
                    uuid, entityName), "Population is negative");

        SingleSimulation simulation = historyManager.getPastSimulation(uuid);
        Entity entity = Utils.findEntityByName(simulation.getWorld(), entityName);

        if (simulation.getOverallPopulation() - entity.getPopulation() + population
                > simulation.getMaxEntitiesAmount())
            return new ResponseDTO(400, String.format("Simulation [%s]: Entity [%s]: Population has not been initialized",
                    uuid, entityName), "Overall population exceeds board!");

        entity.initPopulation(population);

        return new ResponseDTO(200, String.format("Simulation [%s]: Entity [%s]: Population initialized to [%d]",
                uuid, entityName, population));
    }
    public ResponseDTO stopRunningSimulation(String uuid, boolean isFinished) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);
        SimulationState simulationState = simulation.getSimulationState();

        if (simulationState.equals(SimulationState.FINISHED) || simulationState.equals(SimulationState.ERROR))
            return new ResponseDTO(400, String.format("Simulation [%s] was not stopped.", uuid),
                    String.format("Requested simulation's state is [%s]", simulationState.name()));

        simulation.setSimulationState(isFinished ? SimulationState.FINISHED : SimulationState.STOPPED);
        return new ResponseDTO(200, String.format("Simulation [%s] is [%s]", uuid, isFinished ? "terminated" : "stopped"));
    }
    public ResponseDTO resumeStoppedSimulation(String uuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        if (!simulation.getSimulationState().equals(SimulationState.STOPPED))
            return new ResponseDTO(400, String.format("Simulation [%s] was not resumed.", uuid),
                    "Requested simulation is not stopped");

        simulation.setSimulationState(SimulationState.RUNNING);
        return new ResponseDTO(200, String.format("Simulation [%s] is running", uuid));
    }
    private ResponseDTO startSimulation(String uuid) throws Exception {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        if (simulation.getSimulationState() == SimulationState.CREATED)
            setEntitiesInitialLocations(simulation);

        simulation.run();

        if (simulation.getSimulationState() == SimulationState.ERROR) {
            historyManager.getPastSimulations().remove(uuid);
            return new ResponseDTO(500, String.format("Simulation [%s] was removed", uuid), "Simulation runtime error occurred.");
        }

        return new ResponseDTO(200, SimulationState.FINISHED);
    }
    public ResponseDTO getGrid() {
        return new ResponseDTO(200, getInitialWorld().getGrid());
    }
    private void setEntitiesInitialLocations(SingleSimulation simulation) {
        simulation.getWorld()
                .getEntities()
                .getEntitiesMap()
                .values()
                .forEach(entity -> {
                    entity.getSingleEntities().forEach(singleEntity -> {
                        Coordinate randomCoordinate = new Coordinate(RandomGenerator.randomizeRandomNumber(0, getInitialWorld().getGrid().getColumns() - 1),
                                RandomGenerator.randomizeRandomNumber(0, getInitialWorld().getGrid().getRows() - 1));

                        while (isCoordinateTaken(simulation, randomCoordinate)) {
                            randomCoordinate.setX(RandomGenerator.randomizeRandomNumber(0, getInitialWorld().getGrid().getColumns() - 1));
                            randomCoordinate.setY(RandomGenerator.randomizeRandomNumber(0, getInitialWorld().getGrid().getRows() - 1));
                        }

                        singleEntity.setCoordinate(randomCoordinate);
                        changeCoordinateState(simulation, randomCoordinate);
                    });
                });
    }
    private boolean isCoordinateTaken(SingleSimulation simulation, Coordinate coordinate) {
        return simulation.getWorld().getGrid().isTaken(coordinate);
    }
    private void changeCoordinateState(SingleSimulation simulation, Coordinate coordinate) {
        simulation.getWorld().getGrid().changeCoordinateState(coordinate);
    }
    private void setInitialXmlWorld(World initialWorld) {
        historyManager.setInitialXmlWorld(initialWorld);
    }
    private World getInitialWorld() {
        return historyManager.getInitialWorld();
    }
    private SingleSimulationDTO findSimulationDTOByUuid(String uuid) throws JsonProcessingException {
        List<SingleSimulationDTO> pastSimulations = SingletonObjectMapper.objectMapper.readValue(getPastSimulations().getData(),
                new TypeReference<List<SingleSimulationDTO>>() {});

        return pastSimulations.stream().filter(element -> element.getUuid().equals(uuid))
                .findFirst().orElse(null);
    }
}
