package history;

import allocations.AllocationRequest;
import dtos.Mappers;
import other.SingleSimulationDTO;
import exceptions.UUIDNotFoundException;
import prototypes.SingleEntity;
import prototypes.prd.implemented.*;
import prototypes.prd.implemented.Properties;
import simulation.SingleSimulation;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HistoryManager implements Serializable {
    protected Map<String, AllocationRequest> requests;
    protected Map<String, SingleSimulation> pastSimulations;
    protected Map<String, SingleSimulation> creatingSimulations;
    protected Map<String, World> initialWorlds;

    public HistoryManager() {
        pastSimulations = new HashMap<>();
        initialWorlds = new HashMap<>();
        creatingSimulations = new HashMap<>();
        requests = new HashMap<>();
    }

    public void addInitialWorld(World world) {
        initialWorlds.put(world.getName(), world);
    }

    public void addAllocationRequest(AllocationRequest request) {
        requests.put(request.getUuid(), request);
    }

    public void addPastSimulation(SingleSimulation simulation) {
        AllocationRequest request = requests.get(simulation.getRequestUuid());

        if (request.canExecute()) {
            pastSimulations.put(simulation.getUUID(), simulation);
            request.getRequestSimulations().put(simulation.getUUID(), simulation);
        }
    }

    public void addCreatingSimulation(SingleSimulation simulation) {
        creatingSimulations.put(simulation.getUUID(), simulation);
    }

    public void handleFinishedCreatingSimulation(String uuid) {
        addPastSimulation(creatingSimulations.remove(uuid));
    }

    public boolean isAnyXmlLoaded() {
        return !initialWorlds.isEmpty();
    }

    public String createSimulation(String requestUuid) throws Exception {
        AllocationRequest request = requests.get(requestUuid);

        if (Objects.isNull(request))
            throw new UUIDNotFoundException(String.format("Request [%s] not found", requestUuid));

        else if (!request.canExecute())
            throw new Exception(String.format("Request [%s]: used %d/%d simulations", requestUuid,
                    request.getUsedSimulationsAmount(), request.getRequestedExecutions()));

        World world = new World(initialWorlds.get(request.getInitialWorldName()));
        world.setTermination(new Termination(request.getTermination()));

        SingleSimulation sm = new SingleSimulation(request.getCreatedUser(), requestUuid, world);
        addCreatingSimulation(sm);
        return sm.getUUID();
    }

    public String cloneSimulation(String simulationUuid) throws Exception {
        SingleSimulation toClone = pastSimulations.get(simulationUuid);

        if (Objects.isNull(toClone))
            throw new UUIDNotFoundException(String.format("Simulation [%s] not found", simulationUuid));

        String requestUuid = toClone.getRequestUuid();
        AllocationRequest request = requests.get(requestUuid);

        if (Objects.isNull(request))
            throw new UUIDNotFoundException(String.format("Request [%s] not found", requestUuid));

        else if (!request.canExecute())
            throw new Exception(String.format("Request [%s]: used %d/%d simulations", requestUuid,
                    request.getUsedSimulationsAmount(), request.getRequestedExecutions()));

        SingleSimulation cloned = new SingleSimulation(toClone);
        addCreatingSimulation(cloned);
        return cloned.getUUID();
    }

    //#region Getters
    public SingleSimulation getCreatingSimulation(String simulationUuid) { return creatingSimulations.get(simulationUuid); }

    public SingleSimulation getPastSimulation(String simulationUuid) {
        return pastSimulations.get(simulationUuid);
    }

    public Map<String, SingleSimulation> getPastSimulations() {
        return pastSimulations;
    }

    public Map<String, AllocationRequest> getRequests() { return requests; }

    public World getInitialWorld(String initialWorldName) {
        return initialWorlds.get(initialWorldName);
    }

    public List<World> getAllValidWorlds() {
        return new ArrayList<>(initialWorlds.values());
    }

    public boolean isEmpty() {
        return pastSimulations.isEmpty();
    }

    private List<String> getValuesListForProperty(SingleSimulation singleSimulation,
                                                  String entityName, String propertyName) {
        Entity mainEntity = singleSimulation.getLastWorldState().getEntitiesMap().get(entityName);

        if (Objects.isNull(mainEntity))
            return null;

        return mainEntity.getSingleEntities()
                .stream()
                .map(SingleEntity::getProperties)
                .map(Properties::getPropsMap)
                .map(propsMap -> propsMap.get(propertyName))
                .map(Property::getValue)
                .map(Value::getCurrentValue)
                .collect(Collectors.toList());
    }

    public Map<String, Long> getEntitiesCountForProp(String simulationUuid, String entityName,
                                                     String propertyName) {
        SingleSimulation foundSimulation = getPastSimulation(simulationUuid);
        List<String> propertyValues = getValuesListForProperty(foundSimulation, entityName, propertyName);

        if (Objects.isNull(propertyValues))
            return null;

        return propertyValues.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public SingleSimulationDTO getRequestSimulation(String requestUuid, String createdBy) {
        AllocationRequest request = requests.get(requestUuid);
        World world = new World(initialWorlds.get(request.getInitialWorldName()));
        world.setTermination(new Termination(request.getTermination()));

        return new SingleSimulationDTO(requestUuid, createdBy, Mappers.toDto(world));
    }
    //#endregion
}
