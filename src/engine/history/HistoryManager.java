package engine.history;

import engine.exceptions.UUIDNotFoundException;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.Properties;
import engine.simulation.SingleSimulationLog;

import java.util.*;
import java.util.stream.Collectors;

public class HistoryManager {
    protected HashMap<String, SingleSimulationLog> pastSimulations;

    public HistoryManager() {
        pastSimulations = new HashMap<>();
    }
    public void addPastSimulation(SingleSimulationLog pastSimulation) {
        pastSimulations.put(pastSimulation.getUuid().toString(), pastSimulation);
    }
    public SingleSimulationLog getPastSimulation(String uuid) {
        return pastSimulations.get(uuid);
    }

    public void getSimulationDetails(String uuid, SingleSimulationLog simulationLog) throws UUIDNotFoundException {
        if (Objects.isNull(simulationLog))
            throw new UUIDNotFoundException(String.format("No simulation found with uuid [%s]", uuid));

        System.out.printf("Simulation uuid: [%s]%n", uuid);
        System.out.printf("Simulation start time:  [%s]%n", Utils.formatDate(simulationLog.getStartTime()));
    }
    private List<String> getValuesListForProperty(SingleSimulationLog log,
                                                  String entityName, String propertyName) {
        Entity mainEntity = log.getFinishWorldState().getEntitiesMap().get(entityName);

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

    public HashMap<String, Integer> getEntitiesCountForProp(String uuid, String entityName,
                                                            String propertyName) throws UUIDNotFoundException {
        HashMap<String, Integer> histogram = new HashMap<>();

        SingleSimulationLog simulationLog = getPastSimulation(uuid);
        getSimulationDetails(uuid, simulationLog);

        List<String> propertyValues = getValuesListForProperty(simulationLog, entityName, propertyName);

        if (Objects.isNull(propertyValues))
            return null;

        for (String value : propertyValues)
            if (!histogram.containsKey(value))
                histogram.put(value, Collections.frequency(propertyValues, value));

        return histogram;
    }
    public HashMap<String, Integer[]> getEntitiesBeforeAndAfter(String uuid) throws UUIDNotFoundException {
        HashMap<String, Integer[]> entitiesBeforeAfter = new HashMap<>();

        SingleSimulationLog simulationLog = getPastSimulation(uuid);
        getSimulationDetails(uuid, simulationLog);

        simulationLog.getStartWorldState().getEntitiesMap().forEach((key, entity) -> {
            int startAmount = entity.getPopulation();
            Entity finishEntity = simulationLog.getFinishWorldState().getEntitiesMap().get(key);
            int finishAmount =  Objects.isNull(finishEntity) ? 0 : finishEntity.getPopulation();
            Integer[] array = { startAmount, finishAmount };

            entitiesBeforeAfter.put(key, array);
        });

        return entitiesBeforeAfter;
    }
}
