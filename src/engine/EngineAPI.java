package engine;

import dtos.*;
import engine.exceptions.UUIDNotFoundException;
import engine.history.HistoryManager;
import engine.logs.EngineLoggers;
import engine.modules.Utils;
import engine.parsers.XmlParser;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDWorld;
import engine.simulation.SingleSimulation;
import engine.validators.PRDWorldValidators;
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

    //#region XML
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
    //#endregion

    //#region Simulation
    public ResponseDTO createSimulation() {
        SingleSimulation sm = new SingleSimulation(new World(getInitialWorld()));
        historyManager.addPastSimulation(sm);
        return new ResponseDTO(200, sm.getUUID());
    }
    public ResponseDTO cloneSimulation(String uuid) {
        SingleSimulation cloned = new SingleSimulation(historyManager.getPastSimulation(uuid));
        historyManager.addPastSimulation(cloned);
        return new ResponseDTO(200, cloned.getUUID());
    }
    private void runSimulation(String uuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        simulation.run();

        if (simulation.getSimulationState() == SimulationState.ERROR)
            historyManager.getPastSimulations().remove(uuid);
    }
    public ResponseDTO enqueueSimulation(String uuid) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(500, String.format("Simulation [%s] was not executed", uuid), String.format("Simulation [%s] could not be found", uuid));

        threadPoolManager.executeTask(() -> runSimulation(uuid));

        return new ResponseDTO(200, String.format("Simulation [%s] was added to thread pool", uuid));
    }
    public ResponseDTO stopSimulation(String uuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);
        SimulationState simulationState = simulation.getSimulationState();

        if (!simulationState.equals(SimulationState.RUNNING) &&
                !simulationState.equals(SimulationState.PAUSED))
            return new ResponseDTO(400, String.format("Simulation [%s] was not stopped.", uuid),
                    String.format("Requested simulation's state is [%s]", simulationState.name()));

        simulation.setSimulationState(SimulationState.FINISHED);
        return new ResponseDTO(200, String.format("Simulation [%s] is stopped", uuid));
    }
    public ResponseDTO pauseSimulation(String uuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        if (!simulation.getSimulationState().equals(SimulationState.RUNNING))
            return new ResponseDTO(400, String.format("Simulation [%s] was not stopped", uuid),
                    "Simulation is not running");

        simulation.setSimulationState(SimulationState.PAUSED);
        return new ResponseDTO(200, String.format("Simulation [%s] was paused", uuid));
    }
    public ResponseDTO resumeSimulation(String uuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        if (!simulation.getSimulationState().equals(SimulationState.PAUSED))
            return new ResponseDTO(400, String.format("Simulation [%s] was not resumed.", uuid),
                    "Requested simulation is not stopped");

        simulation.setSimulationState(SimulationState.RUNNING);
        return new ResponseDTO(200, String.format("Simulation [%s] is running", uuid));
    }
    public ResponseDTO getSimulationDetails() {
        if (!historyManager.isXmlLoaded())
            return new ResponseDTO(400, "", "No loaded XML");

        return new ResponseDTO(200, historyManager.getMockSimulationForDetails());
    }
    //#endregion

    //#region History
    private void removeSimulationFromHistory(String uuid) {
        historyManager.getPastSimulations().remove(uuid);
    }
    public ResponseDTO removeSimulationIfUnused(String uuid) {
        if (historyManager.getPastSimulation(uuid).getSimulationState() == SimulationState.CREATED) {
            removeSimulationFromHistory(uuid);
            return new ResponseDTO(200, true);
        }

        return new ResponseDTO(200, false);
    }
    public ResponseDTO removeUnusedSimulations() {
        List<SingleSimulation> toRemove = historyManager.getPastSimulations().values()
                .stream()
                .filter(simulation -> simulation.getSimulationState() == SimulationState.CREATED)
                .collect(Collectors.toList());

        toRemove.forEach(simulation -> removeSimulationIfUnused(simulation.getUUID()));

        return new ResponseDTO(200, "");
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
    //#endregion

    //#region Getters & Setters
    public ResponseDTO getGrid() {
        return new ResponseDTO(200, getInitialWorld().getGrid());
    }
    private void setInitialXmlWorld(World initialWorld) {
        historyManager.setInitialXmlWorld(initialWorld);
    }
    private World getInitialWorld() {
        return historyManager.getInitialWorld();
    }
    public ResponseDTO getPastSimulations() {
        if (historyManager.isEmpty())
            return new ResponseDTO(400, Collections.emptyList(), "History is empty!");

        List<SingleSimulationDTO> data = historyManager.getPastSimulations().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(SingleSimulationDTO::getCreatedTimestamp))
                .collect(Collectors.toList());

        return new ResponseDTO(200, data);
    }
    public ResponseDTO getPastSimulation(String uuid) {
        if (historyManager.isEmpty())
            return new ResponseDTO(400, Collections.emptyList(), "History is empty!");

        return new ResponseDTO(200, Mappers.toDto(historyManager.getPastSimulation(uuid)));
    }
    public ResponseDTO getEntities(String uuid, boolean isInitial) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(400, Collections.emptyList(), String.format("UUID [%s] not found", uuid));

        World relevantWorld = isInitial ? getInitialWorld() : historyManager.getPastSimulation(uuid).getWorld();

        List<EntityDTO> data = relevantWorld
                .getEntities()
                .getEntitiesMap()
                .values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(EntityDTO::getName))
                .collect(Collectors.toList());

        return new ResponseDTO(200, data);
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
    public ResponseDTO setEntityInitialPopulation(String uuid, EntityDTO entityDTO, int population) {
        String entityName = entityDTO.getName();

        if (population < 0)
            return new ResponseDTO(400, String.format("Simulation [%s]: Entity [%s]: Population has not been initialized",
                    uuid, entityName), "Population is negative");

        SingleSimulation simulation = historyManager.getPastSimulation(uuid);
        Entity entity = Utils.findEntityByName(simulation.getWorld(), entityName);

        if (simulation.getOverallPopulation() - entity.getPopulation() + population
                > simulation.getMaxEntitiesAmount())
            return new ResponseDTO(400, String.format("Simulation [%s]: Entity [%s]: Population has not been initialized",
                    uuid, entityName), "Overall population exceeds board!");

        entity.initPopulation(simulation.getGrid(), population);
        entityDTO.setPopulation(population);

        return new ResponseDTO(200, String.format("Simulation [%s]: Entity [%s]: Population initialized to [%d]",
                uuid, entityName, population));
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
                prop.setValue(val);
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
            prop.setValue(val);

            return new ResponseDTO(200, String.format("UUID [%s]: Set environment variable [%s]: Value: [%s]",
                    uuid, foundProp.getName(), foundProp.getValue().getCurrentValue()));
        }

        return new ResponseDTO(500, String.format("Environment variable [%s] was not set", prop.getName()),
                String.format("UUID [%s]: Environment variable [%s] not found", uuid, prop.getName()));
    }
    public ResponseDTO getEntitiesAmountsPerTick(String uuid) {
        if (historyManager.isEmpty())
            return new ResponseDTO(400, Collections.emptyList(), "History is empty!");

        else if (historyManager.getPastSimulation(uuid).getSimulationState() != SimulationState.FINISHED)
            return new ResponseDTO(400, 0, "Simulation is not finished!");

        Map<String, List<Integer>> entitiesAmount = new HashMap<>();
        SingleSimulation simulation = historyManager.getPastSimulation(uuid);

        for (Entity entity : simulation.getWorld().getEntities().getEntitiesMap().values()) {
            List<Integer> amounts = new ArrayList<>();

            for (int i = 0; i < simulation.getTicks(); i++)
                amounts.add(simulation.getEntityAmountForTick(entity.getName(), i));

            entitiesAmount.put(entity.getName(), amounts);
        }

        return new ResponseDTO(200, entitiesAmount);
    }
    public ResponseDTO getEntitiesCountForProp(String uuid, String entityName, String propertyName) throws UUIDNotFoundException {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(500, Collections.emptyMap(), String.format("UUID [%s] not found", uuid));

        return new ResponseDTO(200, historyManager.getEntitiesCountForProp(uuid, entityName, propertyName));
    }
    public ResponseDTO getPropertyAverage(String uuid, String entityName, String propertyName) {
        if (Objects.isNull(historyManager.getPastSimulation(uuid)))
            return new ResponseDTO(500, Collections.emptyMap(), String.format("UUID [%s] not found", uuid));

        Entity mainEntity = historyManager.getPastSimulation(uuid).getWorld().getEntities().getEntitiesMap().get(entityName);

        if (!PropTypes.NUMERIC_PROPS.contains(mainEntity.getInitialProperties().getPropsMap().get(propertyName).getType()))
            return new ResponseDTO(400, null, "Property is not numeric");

        double average = mainEntity.getSingleEntities()
                .stream()
                .mapToDouble(element -> Double.parseDouble(element.getProperties().getPropsMap().get(propertyName).getValue().getCurrentValue()))
                .average().orElse(0);

        return new ResponseDTO(200, average);
    }
    public ResponseDTO getQueueManagementDetails() {
        Collection<SingleSimulation> pastSimulations = historyManager.getPastSimulations().values();

        int finishedSimulations = (int)pastSimulations.stream()
                .filter(simulation -> simulation.getSimulationState() == SimulationState.FINISHED)
                .count();

        int runningSimulations = (int)pastSimulations.stream()
                .filter(simulation -> simulation.getSimulationState() == SimulationState.RUNNING)
                .count();

        int pendingSimulations = (int)pastSimulations.stream()
                .filter(simulation -> simulation.getSimulationState() == SimulationState.CREATED)
                .count();

        QueueMgmtDTO queueMgmtDTO = new QueueMgmtDTO(pendingSimulations, runningSimulations, finishedSimulations);

        return new ResponseDTO(200, queueMgmtDTO);
    }
    //#endregion
}
