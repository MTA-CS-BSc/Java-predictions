package engine.history;

import dtos.Mappers;
import dtos.SingleSimulationDTO;
import engine.exceptions.UUIDNotFoundException;
import engine.prototypes.implemented.*;
import engine.simulation.SingleSimulation;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public String createSimulationFromName(String name) {
        SingleSimulation sm = new SingleSimulation(initialWorlds.get(name));
        addPastSimulation(sm);
        return sm.getUUID();
    }
}
