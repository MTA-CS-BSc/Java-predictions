package engine.history;

import engine.exceptions.UUIDNotFoundException;
import engine.logs.EngineLoggers;
import engine.prototypes.implemented.Properties;
import engine.prototypes.implemented.*;
import engine.simulation.SingleSimulation;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HistoryManager implements Serializable {
    protected Map<String, SingleSimulation> pastSimulations;

    public HistoryManager() {
        pastSimulations = new HashMap<>();
    }

    public void addPastSimulation(SingleSimulation pastSimulation) {
        pastSimulations.put(pastSimulation.getUUID(), pastSimulation);
    }

    public SingleSimulation getPastSimulation(String uuid) {
        return pastSimulations.get(uuid);
    }

    private void getSimulationDetails(String uuid, SingleSimulation simulation) throws UUIDNotFoundException {
        if (Objects.isNull(simulation))
            throw new UUIDNotFoundException(String.format("No simulation found with uuid [%s]", uuid));

        EngineLoggers.SIMULATION_LOGGER.info(String.format("Simulation uuid: [%s]%n", uuid));
        EngineLoggers.SIMULATION_LOGGER.info(String.format("Simulation start time: [%s]%n", simulation.getCreatedTimestamp()));
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
        getSimulationDetails(uuid, foundSimulation);
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
}
