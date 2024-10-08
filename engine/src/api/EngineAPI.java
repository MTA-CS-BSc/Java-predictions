package api;

import allocations.AllocationRequest;
import dtos.Mappers;
import exceptions.UUIDNotFoundException;
import history.HistoryManager;
import logs.EngineLoggers;
import modules.Utils;
import other.*;
import parsers.XmlParser;
import prototypes.User;
import prototypes.prd.generated.PRDWorld;
import prototypes.prd.implemented.Entity;
import prototypes.prd.implemented.Property;
import prototypes.prd.implemented.World;
import simulation.SingleSimulation;
import threads.ThreadPoolManager;
import types.*;
import validators.PRDWorldValidators;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class EngineAPI {
    protected HistoryManager historyManager;
    protected ThreadPoolManager threadPoolManager;
    protected HashMap<String, User> users;

    public EngineAPI() {
        historyManager = new HistoryManager();
        threadPoolManager = new ThreadPoolManager();
        users = new HashMap<>();
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
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, "Validation failed", "Name exists");

        if (Objects.isNull(validateWorldResponse.getErrorDescription()))
            historyManager.addInitialWorld(new World(prdWorld));

        return validateWorldResponse;
    }

    public ResponseDTO getAllValidWorlds() {
        if (!historyManager.isAnyXmlLoaded())
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, "", "No loaded XML");

        List<WorldDTO> validWorlds = historyManager.getAllValidWorlds()
                .stream()
                .map(Mappers::toDto)
                .collect(Collectors.toList());

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, validWorlds);
    }
    //#endregion

    //#region Allocations
    public ResponseDTO createAllocationRequest(String initialWorldName, int requestedExecutions,
                                               String createdUser, TerminationDTO termination) {
        AllocationRequest request = new AllocationRequest(initialWorldName, requestedExecutions, createdUser, termination);
        historyManager.addAllocationRequest(request);
        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, request.getUuid());
    }

    private ResponseDTO setRequestState(String requestUuid, RequestState state) {
        AllocationRequest request = historyManager.getRequests().get(requestUuid);

        if (Objects.isNull(request))
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, "Can't approve request", String.format("Request [%s] not found", requestUuid));

        else if (request.getState() != RequestState.CREATED)
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, "Can't approve request", "Request is already in state " + request.getState().name());

        request.setState(state);
        return new ResponseDTO(ApiConstants.API_RESPONSE_OK);
    }

    public ResponseDTO approveRequest(String requestUuid) {
        return setRequestState(requestUuid, RequestState.APPROVED);
    }

    public ResponseDTO declineRequest(String requestUuid) {
        return setRequestState(requestUuid, RequestState.DECLINED);
    }

    public ResponseDTO getAllocationRequests() {
        List<AllocationRequestDTO> requests = historyManager.getRequests().values()
                .stream()
                .map(Mappers::toDto)
                .collect(Collectors.toList());

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, requests);
    }

    public ResponseDTO getAllocationRequestsForUser(String username) {
        List<AllocationRequestDTO> requests = historyManager.getRequests().values()
                .stream()
                .filter(element -> element.getCreatedUser().equals(username))
                .map(Mappers::toDto)
                .collect(Collectors.toList());

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, requests);
    }
    //#endregion

    //#region History
    public ResponseDTO getPastSimulations() {
        if (historyManager.isEmpty())
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, Collections.emptyList(), "History is empty!");

        List<SingleSimulationDTO> data = historyManager.getPastSimulations().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(SingleSimulationDTO::getCreatedTimestamp))
                .collect(Collectors.toList());

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, data);
    }

    public ResponseDTO getPastSimulationsForUser(String username) {
        if (historyManager.isEmpty())
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, Collections.emptyList(), "History is empty!");

        List<SingleSimulationDTO> data = historyManager.getPastSimulations().values()
                .stream()
                .filter(element -> element.getCreatedUser().equals(username))
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(SingleSimulationDTO::getCreatedTimestamp))
                .collect(Collectors.toList());

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, data);
    }

    public ResponseDTO getPastSimulation(String simulationUuid) {
        if (historyManager.isEmpty())
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, Collections.emptyList(), "History is empty!");

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, Mappers.toDto(historyManager.getPastSimulation(simulationUuid)));
    }

    public ResponseDTO getCreatingSimulation(String simulationUuid) {
        SingleSimulation simulation = historyManager.getCreatingSimulation(simulationUuid);

        if (Objects.isNull(simulation))
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, "", "Can't find simulation");

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, Mappers.toDto(simulation));
    }
    //#endregion

    //#region Simulation
    public ResponseDTO createSimulation(String requestUuid) {
        try {
            return new ResponseDTO(ApiConstants.API_RESPONSE_OK, historyManager.createSimulation(requestUuid));
        } catch (Exception e) {
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, "Simulation was not created", e.getMessage());
        }
    }

    public ResponseDTO cloneSimulation(String simulationUuid) {
        try {
            return new ResponseDTO(ApiConstants.API_RESPONSE_OK, historyManager.cloneSimulation(simulationUuid));
        } catch (Exception e) {
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, "Simulation was not created", e.getMessage());
        }
    }

    private void runSimulation(String simulationUuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(simulationUuid);

        simulation.run();

        if (simulation.getSimulationState() == SimulationState.ERROR)
            historyManager.getPastSimulations().remove(simulationUuid);
    }

    public ResponseDTO enqueueSimulation(String simulationUuid) {
        if (Objects.isNull(historyManager.getCreatingSimulation(simulationUuid)))
            return new ResponseDTO(ApiConstants.API_RESPONSE_SERVER_ERROR, String.format("Simulation [%s] was not executed", simulationUuid), String.format("Simulation [%s] could not be found", simulationUuid));

        historyManager.handleFinishedCreatingSimulation(simulationUuid);
        threadPoolManager.addRunSimulationToQueue(() -> runSimulation(simulationUuid), simulationUuid);

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, String.format("Simulation [%s] was added to thread pool", simulationUuid));
    }

    public ResponseDTO stopSimulation(String simulationUuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(simulationUuid);
        SimulationState simulationState = simulation.getSimulationState();

        if (!simulationState.equals(SimulationState.RUNNING) &&
                !simulationState.equals(SimulationState.PAUSED))
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, String.format("Simulation [%s] was not stopped.", simulationUuid),
                    String.format("Requested simulation's state is [%s]", simulationState.name()));

        simulation.setSimulationState(SimulationState.FINISHED);
        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, String.format("Simulation [%s] is stopped", simulationUuid));
    }

    public ResponseDTO pauseSimulation(String simulationUuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(simulationUuid);

        if (!simulation.getSimulationState().equals(SimulationState.RUNNING))
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, String.format("Simulation [%s] was not stopped", simulationUuid),
                    "Simulation is not running");

        simulation.setSimulationState(SimulationState.PAUSED);
        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, String.format("Simulation [%s] was paused", simulationUuid));
    }

    public ResponseDTO resumeSimulation(String simulationUuid) {
        SingleSimulation simulation = historyManager.getPastSimulation(simulationUuid);

        if (!simulation.getSimulationState().equals(SimulationState.PAUSED))
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, String.format("Simulation [%s] was not resumed.", simulationUuid),
                    "Requested simulation is not paused");

        simulation.setSimulationState(SimulationState.RUNNING);
        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, String.format("Simulation [%s] is running", simulationUuid));
    }

    private void removeSimulation(SingleSimulation simulation) {
        String simulationUuid = simulation.getUUID();

        historyManager.getRequests().get(simulation.getRequestUuid()).getRequestSimulations().remove(simulationUuid);
        historyManager.getPastSimulations().remove(simulationUuid);
    }
    //#endregion

    //#region Simulation Getters & Setters
    public ResponseDTO getSimulationGrid(String simulationUuid) {
        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, historyManager.getPastSimulation(simulationUuid).getGrid());
    }

    public ResponseDTO setEntityInitialPopulation(String simulationUuid, String entityName, int population) {
        if (population < 0)
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, String.format("Simulation [%s]: Entity [%s]: Population has not been initialized",
                    simulationUuid, entityName), "Population is negative");

        SingleSimulation simulation = historyManager.getCreatingSimulation(simulationUuid);
        Entity entity = Utils.findEntityByName(simulation.getWorld(), entityName);

        if (simulation.getOverallPopulation() - entity.getPopulation() + population
                > simulation.getMaxEntitiesAmount())
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, String.format("Simulation [%s]: Entity [%s]: Population has not been initialized",
                    simulationUuid, entityName), "Overall population exceeds board!");

        entity.setInitialPopulation(population);

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, String.format("Simulation [%s]: Entity [%s]: Population initialized to [%d]",
                simulationUuid, entityName, population));
    }

    public ResponseDTO setEnvironmentVariable(String simulationUuid, String envPropertyName, String val) {
        SingleSimulation simulation = historyManager.getCreatingSimulation(simulationUuid);

        if (Objects.isNull(simulation))
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, "UUID", UUIDNotFoundException.class.getSimpleName());

        Property foundProp = Utils.findEnvironmentPropertyByName(simulation.getWorld(), envPropertyName);

        if (!Objects.isNull(foundProp)) {
            if (val == null) {
                foundProp.getValue().setRandomInitialize(true);
                foundProp.getValue().setInit(null);
                foundProp.getValue().setCurrentValue(null);
                return new ResponseDTO(ApiConstants.API_RESPONSE_OK, String.format("UUID [%s]: Reset environment variable [%s]",
                        simulationUuid, foundProp.getName()));
            }

            else if (!TypesUtils.validateType(foundProp.getType(), val))
                return new ResponseDTO(ApiConstants.API_RESPONSE_SERVER_ERROR, String.format("Environment variable [%s] was not set", envPropertyName), "Incorrect type");

            else if (!Utils.validateValueInRange(foundProp, val))
                return new ResponseDTO(ApiConstants.API_RESPONSE_SERVER_ERROR, String.format("Environment variable [%s] was not set", envPropertyName), "Value not in range");

            if (PropTypes.NUMERIC_PROPS.contains(foundProp.getType()))
                val = TypesUtils.removeExtraZeroes(val);

            foundProp.getValue().setRandomInitialize(false);
            foundProp.getValue().setInit(val);
            foundProp.getValue().setCurrentValue(foundProp.getValue().getInit());

            return new ResponseDTO(ApiConstants.API_RESPONSE_OK, String.format("UUID [%s]: Set environment variable [%s]: Value: [%s]",
                    simulationUuid, foundProp.getName(), foundProp.getValue().getCurrentValue()));
        }

        return new ResponseDTO(ApiConstants.API_RESPONSE_SERVER_ERROR, String.format("Environment variable [%s] was not set", envPropertyName),
                String.format("UUID [%s]: Environment variable [%s] not found", simulationUuid, envPropertyName));
    }

    public ResponseDTO setByStep(String simulationUuid, ByStep byStep) {
        if (Objects.isNull(historyManager.getPastSimulation(simulationUuid)))
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, Collections.emptyMap(), "Simulation not found!");

        else if (historyManager.getPastSimulation(simulationUuid).getSimulationState() != SimulationState.PAUSED)
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, 0, "Simulation is not paused!");

        historyManager.getPastSimulation(simulationUuid).setByStep(byStep);
        return new ResponseDTO(ApiConstants.API_RESPONSE_OK);
    }
    //#endregion

    //#region Results
    public ResponseDTO getPropertyConsistency(String simulationUuid, String entityName, String propertyName) {
        SingleSimulation simulation = historyManager.getPastSimulation(simulationUuid);

        if (Objects.isNull(simulation))
            return new ResponseDTO(ApiConstants.API_RESPONSE_SERVER_ERROR, Collections.emptyMap(), String.format("UUID [%s] not found", simulationUuid));

        double returnValue = simulation.getLastWorldState().getEntitiesMap().get(entityName).getSingleEntities()
                .stream().mapToDouble(singleEntity -> singleEntity.getProperties().getPropsMap().get(propertyName).getConsistency(simulation.getTicks()))
                .average().orElse(simulation.getTicks());

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, returnValue);
    }

    public ResponseDTO getEntitiesAmountsPerTick(String simulationUuid) {
        if (Objects.isNull(historyManager.getPastSimulation(simulationUuid)))
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, Collections.emptyMap(), "Simulation not found!");

        else if (historyManager.getPastSimulation(simulationUuid).getSimulationState() != SimulationState.FINISHED)
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, 0, "Simulation is not finished!");

        Map<String, List<Integer>> entitiesAmount = new HashMap<>();
        SingleSimulation simulation = historyManager.getPastSimulation(simulationUuid);

        for (Entity entity : simulation.getWorld().getEntities().getEntitiesMap().values()) {
            List<Integer> amounts = new ArrayList<>();

            for (int i = 0; i < simulation.getTicks(); i++)
                amounts.add(simulation.getEntityAmountForTick(entity.getName(), i));

            entitiesAmount.put(entity.getName(), amounts);
        }

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, entitiesAmount);
    }

    public ResponseDTO getEntitiesCountForProp(String simulationUuid, String entityName, String propertyName) {
        if (Objects.isNull(historyManager.getPastSimulation(simulationUuid)))
            return new ResponseDTO(ApiConstants.API_RESPONSE_SERVER_ERROR, Collections.emptyMap(), String.format("UUID [%s] not found", simulationUuid));

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, historyManager.getEntitiesCountForProp(simulationUuid, entityName, propertyName));
    }

    public ResponseDTO getPropertyAverage(String simulationUuid, String entityName, String propertyName) {
        if (Objects.isNull(historyManager.getPastSimulation(simulationUuid)))
            return new ResponseDTO(ApiConstants.API_RESPONSE_SERVER_ERROR, Collections.emptyMap(), String.format("UUID [%s] not found", simulationUuid));

        Entity mainEntity = historyManager.getPastSimulation(simulationUuid).getWorld().getEntities().getEntitiesMap().get(entityName);

        if (!PropTypes.NUMERIC_PROPS.contains(mainEntity.getInitialProperties().getPropsMap().get(propertyName).getType()))
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, null, "Property is not numeric");

        double average = mainEntity.getSingleEntities()
                .stream()
                .mapToDouble(element -> Double.parseDouble(element.getProperties().getPropsMap().get(propertyName).getValue().getCurrentValue()))
                .average().orElse(0);

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, average);
    }
    //#endregion

    //#region Thread pool
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

        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, queueMgmtDTO);
    }

    public ResponseDTO setThreadsAmount(int amount) {
        if (amount <= 0)
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, "Amount not set", "Amount should be positive");

        threadPoolManager.setThreadsAmount(amount);
        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, true);
    }

    public ResponseDTO getThreadsAmount() {
        return new ResponseDTO(ApiConstants.API_RESPONSE_OK, threadPoolManager.getThreadsAmount());
    }
    //#endregion

    //#region Users
    public ResponseDTO createUser(String username, boolean isConencted) {
        if (users.containsKey(username))
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, "User was not created", String.format("Username [%s] exists", username));

        users.put(username, new User(username, isConencted));
        return new ResponseDTO(ApiConstants.API_RESPONSE_OK);
    }

    public ResponseDTO setUserConnected(String username, boolean isConnected) {
        if (!users.containsKey(username))
            return new ResponseDTO(ApiConstants.API_RESPONSE_BAD_REQUEST, "Can't perform action", String.format("User [%s] does not exist", username));

        users.get(username).setIsConnected(isConnected);
        return new ResponseDTO(ApiConstants.API_RESPONSE_OK);
    }
    //#endregion
}
