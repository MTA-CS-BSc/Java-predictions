package api;

import dtos.Mappers;
import exceptions.UUIDNotFoundException;
import history.HistoryManager;
import logs.EngineLoggers;
import modules.Constants;
import modules.Utils;
import other.*;
import parsers.XmlParser;
import prototypes.implemented.Entity;
import prototypes.implemented.Property;
import prototypes.implemented.World;
import prototypes.jaxb.PRDWorld;
import types.ByStep;
import simulation.SingleSimulation;
import validators.PRDWorldValidators;
import threads.ThreadPoolManager;
import types.PropTypes;
import types.SimulationState;
import types.TypesUtils;

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

    //#region XML
    public ResponseDTO loadXml(InputStream fileStream) throws JAXBException, IOException {
        PRDWorld prdWorld = XmlParser.parseWorldXml(fileStream);
        ResponseDTO validateWorldResponse = PRDWorldValidators.validateWorld(prdWorld);

        if (!Objects.isNull(historyManager.getInitialWorld(prdWorld.getName())))
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, "Validation failed", "Name exists");

        if (Objects.isNull(validateWorldResponse.getErrorDescription()))
            historyManager.addInitialWorld(new World(prdWorld));

        return validateWorldResponse;
    }

    public ResponseDTO anyXmlLoaded() {
        return new ResponseDTO(Constants.API_RESPONSE_OK, historyManager.anyXmlLoaded());
    }
    //#endregion

    //#region Simulation
    public ResponseDTO createSimulation(String fromInitialName) {
        return new ResponseDTO(Constants.API_RESPONSE_OK, historyManager.createSimulationFromName(fromInitialName));
    }

    public ResponseDTO cloneSimulation(String uuid) {
        SingleSimulation cloned = new SingleSimulation(historyManager.getPastSimulation(uuid));
        historyManager.addPastSimulation(cloned);
        return new ResponseDTO(Constants.API_RESPONSE_OK, cloned.getUUID());
    }

    private void runSimulation(String uuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        simulation.run();

        if (simulation.getSimulationState() == SimulationState.ERROR)
            historyManager.getPastSimulations().remove(uuid);
    }

    public ResponseDTO enqueueSimulation(String uuid) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(Constants.API_RESPONSE_SERVER_ERROR, String.format("Simulation [%s] was not executed", uuid), String.format("Simulation [%s] could not be found", uuid));

        threadPoolManager.addRunSimulationToQueue(() -> runSimulation(uuid), uuid);

        return new ResponseDTO(Constants.API_RESPONSE_OK, String.format("Simulation [%s] was added to thread pool", uuid));
    }

    public ResponseDTO stopSimulation(String uuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);
        SimulationState simulationState = simulation.getSimulationState();

        if (!simulationState.equals(SimulationState.RUNNING) &&
                !simulationState.equals(SimulationState.PAUSED))
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, String.format("Simulation [%s] was not stopped.", uuid),
                    String.format("Requested simulation's state is [%s]", simulationState.name()));

        simulation.setSimulationState(SimulationState.FINISHED);
        return new ResponseDTO(Constants.API_RESPONSE_OK, String.format("Simulation [%s] is stopped", uuid));
    }

    public ResponseDTO pauseSimulation(String uuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        if (!simulation.getSimulationState().equals(SimulationState.RUNNING))
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, String.format("Simulation [%s] was not stopped", uuid),
                    "Simulation is not running");

        simulation.setSimulationState(SimulationState.PAUSED);
        return new ResponseDTO(Constants.API_RESPONSE_OK, String.format("Simulation [%s] was paused", uuid));
    }

    public ResponseDTO resumeSimulation(String uuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        if (!simulation.getSimulationState().equals(SimulationState.PAUSED))
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, String.format("Simulation [%s] was not resumed.", uuid),
                    "Requested simulation is not stopped");

        simulation.setSimulationState(SimulationState.RUNNING);
        return new ResponseDTO(Constants.API_RESPONSE_OK, String.format("Simulation [%s] is running", uuid));
    }

    public ResponseDTO getSimulationDetails(String fromInitialName) {
        if (!historyManager.anyXmlLoaded())
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, "", "No loaded XML");

        return new ResponseDTO(Constants.API_RESPONSE_OK, historyManager.getSimulationDetails(fromInitialName));
    }

    public ResponseDTO getAllSimulationsDetails() {
        if (!historyManager.anyXmlLoaded())
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, "", "No loaded XML");

        return new ResponseDTO(Constants.API_RESPONSE_OK,  historyManager.getAllSimulationsDetails());
    }

    public ResponseDTO getAllValidWorldsNames() {
        if (!historyManager.anyXmlLoaded())
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, "", "No loaded XML");

        return new ResponseDTO(Constants.API_RESPONSE_OK, historyManager.getAllValidWorldsNames());
    }
    //#endregion

    //#region History
    private void removeSimulation(SingleSimulation simulation) {
        historyManager.getPastSimulations().remove(simulation.getUUID());
    }

    public ResponseDTO removeUnusedSimulations() {
        List<SingleSimulation> toRemove = historyManager.getPastSimulations().values()
                .stream()
                .filter(simulation -> simulation.getSimulationState() == SimulationState.CREATED && !threadPoolManager.isSimulationRecorded(simulation.getUUID()))
                .collect(Collectors.toList());

        toRemove.forEach(this::removeSimulation);

        return new ResponseDTO(Constants.API_RESPONSE_OK, "");
    }

    public ResponseDTO isHistoryEmpty() {
        return new ResponseDTO(Constants.API_RESPONSE_OK, historyManager.isEmpty());
    }

    public ResponseDTO writeHistoryToFile(String filePath) {
        try {
            FileOutputStream f = new FileOutputStream(filePath);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(historyManager);
            o.close();
            f.close();
            EngineLoggers.API_LOGGER.info("History saved successfully.");
            return new ResponseDTO(Constants.API_RESPONSE_OK, true);
        } catch (Exception e) {
            EngineLoggers.API_LOGGER.info("Could not save history: " + e.getMessage());
            return new ResponseDTO(Constants.API_RESPONSE_OK, false);
        }
    }

    public ResponseDTO loadHistory(String filePath) {
        try {
            FileInputStream fi = new FileInputStream(filePath);
            ObjectInputStream oi = new ObjectInputStream(fi);
            historyManager = (HistoryManager) oi.readObject();
            return new ResponseDTO(Constants.API_RESPONSE_OK);
        } catch (Exception e) {
            EngineLoggers.API_LOGGER.info(e.getMessage());
            return new ResponseDTO(Constants.API_RESPONSE_SERVER_ERROR, "Attempted to loader history but no history file was found");
        }
    }
    //#endregion

    //#region Getters & Setters
    public ResponseDTO getGrid(String uuid) {
        return new ResponseDTO(Constants.API_RESPONSE_OK, historyManager.getPastSimulation(uuid).getGrid());
    }

    public ResponseDTO getPastSimulations() {
        if (historyManager.isEmpty())
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, Collections.emptyList(), "History is empty!");

        List<SingleSimulationDTO> data = historyManager.getPastSimulations().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(SingleSimulationDTO::getCreatedTimestamp))
                .collect(Collectors.toList());

        return new ResponseDTO(Constants.API_RESPONSE_OK, data);
    }

    public ResponseDTO getPastSimulation(String uuid) {
        if (historyManager.isEmpty())
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, Collections.emptyList(), "History is empty!");

        return new ResponseDTO(Constants.API_RESPONSE_OK, Mappers.toDto(historyManager.getPastSimulation(uuid)));
    }

    public ResponseDTO getEntities(String uuid, boolean isInitial) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        if (Objects.isNull(simulation))
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, Collections.emptyList(), String.format("UUID [%s] not found", uuid));

        World relevantWorld;

        if (isInitial)
            relevantWorld = new World(simulation.getWorld(), simulation.getStartWorldState());

        else
            relevantWorld = simulation.getWorld();

        List<EntityDTO> data = relevantWorld
                .getEntities()
                .getEntitiesMap()
                .values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(EntityDTO::getName))
                .collect(Collectors.toList());

        return new ResponseDTO(Constants.API_RESPONSE_OK, data);
    }

    public ResponseDTO getEnvironmentProperties(String uuid) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(Constants.API_RESPONSE_SERVER_ERROR, Collections.emptyList(), String.format("UUID [%s] not found", uuid));

        List<PropertyDTO> data = historyManager.getPastSimulation(uuid).getWorld().getEnvironment()
                .getEnvVars().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(PropertyDTO::getName))
                .collect(Collectors.toList());

        return new ResponseDTO(Constants.API_RESPONSE_OK, data);
    }

    public ResponseDTO setEntityInitialPopulation(String uuid, EntityDTO entityDTO, int population) {
        String entityName = entityDTO.getName();

        if (population < 0)
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, String.format("Simulation [%s]: Entity [%s]: Population has not been initialized",
                    uuid, entityName), "Population is negative");

        SingleSimulation simulation = historyManager.getPastSimulation(uuid);
        Entity entity = Utils.findEntityByName(simulation.getWorld(), entityName);

        if (simulation.getOverallPopulation() - entity.getPopulation() + population
                > simulation.getMaxEntitiesAmount())
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, String.format("Simulation [%s]: Entity [%s]: Population has not been initialized",
                    uuid, entityName), "Overall population exceeds board!");

        entity.initPopulation(simulation.getGrid(), population);
        entityDTO.setPopulation(population);

        return new ResponseDTO(Constants.API_RESPONSE_OK, String.format("Simulation [%s]: Entity [%s]: Population initialized to [%d]",
                uuid, entityName, population));
    }

    public ResponseDTO setEnvironmentVariable(String uuid, PropertyDTO prop, String val) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, "UUID", UUIDNotFoundException.class.getSimpleName());

        Property foundProp = Utils.findEnvironmentPropertyByName(historyManager.getPastSimulation(uuid).getWorld(), prop.getName());

        if (!Objects.isNull(foundProp)) {
            if (val == null) {
                // UI clear button reset
                foundProp.getValue().setRandomInitialize(true);
                foundProp.getValue().setInit(null);
                foundProp.getValue().setCurrentValue(null);
                prop.setValue(val);
                return new ResponseDTO(Constants.API_RESPONSE_OK, String.format("UUID [%s]: Reset environment variable [%s]",
                        uuid, foundProp.getName()));
            }

            else if (!TypesUtils.validateType(prop.getType(), val))
                return new ResponseDTO(Constants.API_RESPONSE_SERVER_ERROR, String.format("Environment variable [%s] was not set", prop.getName()), "Incorrect type");

            else if (!Utils.validateValueInRange(prop, val))
                return new ResponseDTO(Constants.API_RESPONSE_SERVER_ERROR, String.format("Environment variable [%s] was not set", prop.getName()), "Value not in range");

            if (PropTypes.NUMERIC_PROPS.contains(foundProp.getType()))
                val = TypesUtils.removeExtraZeroes(val);

            foundProp.getValue().setRandomInitialize(false);
            foundProp.getValue().setInit(val);
            foundProp.getValue().setCurrentValue(foundProp.getValue().getInit());
            prop.setValue(val);

            return new ResponseDTO(Constants.API_RESPONSE_OK, String.format("UUID [%s]: Set environment variable [%s]: Value: [%s]",
                    uuid, foundProp.getName(), foundProp.getValue().getCurrentValue()));
        }

        return new ResponseDTO(Constants.API_RESPONSE_SERVER_ERROR, String.format("Environment variable [%s] was not set", prop.getName()),
                String.format("UUID [%s]: Environment variable [%s] not found", uuid, prop.getName()));
    }

    public ResponseDTO getEntitiesAmountsPerTick(String uuid) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, Collections.emptyMap(), "Simulation not found!");

        else if (historyManager.getPastSimulation(uuid).getSimulationState() != SimulationState.FINISHED)
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, 0, "Simulation is not finished!");

        Map<String, List<Integer>> entitiesAmount = new HashMap<>();
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        for (Entity entity : simulation.getWorld().getEntities().getEntitiesMap().values()) {
            List<Integer> amounts = new ArrayList<>();

            for (int i = 0; i < simulation.getTicks(); i++)
                amounts.add(simulation.getEntityAmountForTick(entity.getName(), i));

            entitiesAmount.put(entity.getName(), amounts);
        }

        return new ResponseDTO(Constants.API_RESPONSE_OK, entitiesAmount);
    }

    public ResponseDTO getEntitiesCountForProp(String uuid, String entityName, String propertyName) throws UUIDNotFoundException {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(Constants.API_RESPONSE_SERVER_ERROR, Collections.emptyMap(), String.format("UUID [%s] not found", uuid));

        return new ResponseDTO(Constants.API_RESPONSE_OK, historyManager.getEntitiesCountForProp(uuid, entityName, propertyName));
    }

    public ResponseDTO getPropertyAverage(String uuid, String entityName, String propertyName) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(Constants.API_RESPONSE_SERVER_ERROR, Collections.emptyMap(), String.format("UUID [%s] not found", uuid));

        Entity mainEntity = historyManager.getPastSimulation(uuid).getWorld().getEntities().getEntitiesMap().get(entityName);

        if (!PropTypes.NUMERIC_PROPS.contains(mainEntity.getInitialProperties().getPropsMap().get(propertyName).getType()))
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, null, "Property is not numeric");

        double average = mainEntity.getSingleEntities()
                .stream()
                .mapToDouble(element -> Double.parseDouble(element.getProperties().getPropsMap().get(propertyName).getValue().getCurrentValue()))
                .average().orElse(0);

        return new ResponseDTO(Constants.API_RESPONSE_OK, average);
    }

    public ResponseDTO getQueueManagementDetails() {
        Collection<SingleSimulation> pastSimulations = historyManager.getPastSimulations().values();

        int finishedSimulations = (int) pastSimulations.stream()
                .filter(simulation -> simulation.getSimulationState() == SimulationState.FINISHED)
                .count();

        int runningSimulations = (int) pastSimulations.stream()
                .filter(simulation -> Arrays.asList(SimulationState.RUNNING, SimulationState.PAUSED).contains(simulation.getSimulationState()))
                .count();

        int pendingSimulations = (int) pastSimulations.stream()
                .filter(simulation -> simulation.getSimulationState() == SimulationState.CREATED)
                .count();

        QueueMgmtDTO queueMgmtDTO = new QueueMgmtDTO(pendingSimulations, runningSimulations, finishedSimulations, threadPoolManager.getThreadsAmount());

        return new ResponseDTO(Constants.API_RESPONSE_OK, queueMgmtDTO);
    }

    public ResponseDTO setByStep(String uuid, ByStep byStep) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, Collections.emptyMap(), "Simulation not found!");

        else if (historyManager.getPastSimulation(uuid).getSimulationState() != SimulationState.PAUSED)
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, 0, "Simulation is not paused!");

        historyManager.getPastSimulation(uuid).setByStep(byStep);
        return new ResponseDTO(Constants.API_RESPONSE_OK, "");
    }

    public ResponseDTO getPropertyConsistency(String uuid, String entityName, String propertyName) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        if (Objects.isNull(simulation))
            return new ResponseDTO(Constants.API_RESPONSE_SERVER_ERROR, Collections.emptyMap(), String.format("UUID [%s] not found", uuid));

        double returnValue = simulation.getLastWorldState().getEntitiesMap().get(entityName).getSingleEntities()
                .stream().mapToDouble(singleEntity -> singleEntity.getProperties().getPropsMap().get(propertyName).getConsistency(simulation.getTicks()))
                .average().orElse(simulation.getTicks());



        return new ResponseDTO(Constants.API_RESPONSE_OK, returnValue);
    }

    public ResponseDTO setThreadsAmount(int amount) {
        if (amount <= 0)
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, "Amount not set", "Amount should be positive");

        threadPoolManager.setThreadsAmount(amount);
        return new ResponseDTO(Constants.API_RESPONSE_OK, true);
    }

    public ResponseDTO getThreadsAmount() {
        return new ResponseDTO(Constants.API_RESPONSE_OK, threadPoolManager.getThreadsAmount());
    }
    //#endregion
}
