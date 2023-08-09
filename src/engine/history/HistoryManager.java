package engine.history;

import engine.exceptions.UUIDNotFoundException;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.Properties;
import engine.simulation.SingleSimulation;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HistoryManager {
    protected Map<String, SingleSimulation> pastSimulations;

    public HistoryManager() {
        pastSimulations = new HashMap<>();
    }
    public void addPastSimulation(SingleSimulation pastSimulation) {
        pastSimulations.put(pastSimulation.getUUID().toString(), pastSimulation);
    }
    public SingleSimulation getPastSimulation(String uuid) {
        return pastSimulations.get(uuid);
    }

    public void getSimulationDetails(String uuid, SingleSimulation simulation) throws UUIDNotFoundException {
        if (Objects.isNull(simulation))
            throw new UUIDNotFoundException(String.format("No simulation found with uuid [%s]", uuid));

        Loggers.SIMULATION_LOGGER.info(String.format("Simulation uuid: [%s]%n", uuid));
        Loggers.SIMULATION_LOGGER.info(String.format("Simulation start time:  [%s]%n", Utils.formatDate(simulation.getStartTime())));
    }
    private List<String> getValuesListForProperty(SingleSimulation singleSimulation,
                                                  String entityName, String propertyName) {
        Entity mainEntity = singleSimulation.getFinishWorldState().getEntitiesMap().get(entityName);

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
    public Map<String, Integer[]> getEntitiesBeforeAndAfter(String uuid) throws UUIDNotFoundException {
        Map<String, Integer[]> entitiesBeforeAfter = new HashMap<>();

        SingleSimulation simulation = getPastSimulation(uuid);
        getSimulationDetails(uuid, simulation);

        simulation.getStartWorldState().getEntitiesMap().forEach((key, entity) -> {
            Entity finishEntity = simulation.getFinishWorldState().getEntitiesMap().get(key);
            int finishAmount =  Objects.isNull(finishEntity) ? 0 : finishEntity.getPopulation();
            Integer[] array = { entity.getPopulation(), finishAmount };

            entitiesBeforeAfter.put(key, array);
        });

        return entitiesBeforeAfter;
    }
}
