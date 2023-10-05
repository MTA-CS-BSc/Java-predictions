package history;

import dtos.Mappers;
import other.SingleSimulationDTO;
import exceptions.UUIDNotFoundException;
import prototypes.implemented.*;
import prototypes.implemented.Properties;
import simulation.SingleSimulation;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HistoryManager implements Serializable {
    protected Map<String, SingleSimulation> pastSimulations;
    protected Map<String, World> initialWorlds;

    public HistoryManager() {
        pastSimulations = new HashMap<>();
        initialWorlds = new HashMap<>();
    }

    public void addInitialWorld(World world) {
        initialWorlds.put(world.getName(), world);
    }

    public boolean anyXmlLoaded() {
        return !initialWorlds.isEmpty();
    }

    public void addPastSimulation(SingleSimulation pastSimulation) {
        pastSimulations.put(pastSimulation.getUUID(), pastSimulation);
    }

    public SingleSimulation getPastSimulation(String uuid) {
        return pastSimulations.get(uuid);
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

    public Map<String, Long> getEntitiesCountForProp(String uuid, String entityName,
                                                     String propertyName) throws UUIDNotFoundException {
        SingleSimulation foundSimulation = getPastSimulation(uuid);
        List<String> propertyValues = getValuesListForProperty(foundSimulation, entityName, propertyName);

        if (Objects.isNull(propertyValues))
            return null;

        return propertyValues.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public boolean isEmpty() {
        return pastSimulations.isEmpty();
    }

    public Map<String, SingleSimulation> getPastSimulations() {
        return pastSimulations;
    }

    public SingleSimulationDTO getSimulationDetails(String fromInitialName) {
        return new SingleSimulationDTO(Mappers.toDto(initialWorlds.get(fromInitialName)));
    }

    public List<SingleSimulationDTO> getAllSimulationsDetails() {
        return initialWorlds.values().stream()
                .map(world -> getSimulationDetails(world.getName()))
                .collect(Collectors.toList());
    }

    public List<World> getAllValidWorlds() {
        return new ArrayList<>(initialWorlds.values());
    }

    public String createSimulation(String initialWorldName, String createdUser, String requestUuid) {
        SingleSimulation sm = new SingleSimulation(createdUser, requestUuid, initialWorlds.get(initialWorldName));
        addPastSimulation(sm);
        return sm.getUUID();
    }

    public World getInitialWorld(String fromInitialName) {
        return initialWorlds.get(fromInitialName);
    }
}
