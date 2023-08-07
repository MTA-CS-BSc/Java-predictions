package engine.history;

import engine.logs.Loggers;
import engine.modules.Utils;
import engine.prototypes.implemented.Entity;
import engine.simulation.SingleSimulationLog;

import java.util.HashMap;
import java.util.Objects;

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

    public void getSimulationDetails(String uuid, SingleSimulationLog simulationLog) {
        if (Objects.isNull(simulationLog)) {
            Loggers.HISTORY_LOGGER.info(String.format("No simulation found with uuid [%s]", uuid));
            return;
        }

        System.out.printf("Simulation uuid: [%s]%n", uuid);
        System.out.printf("Simulation start time:  [%s]%n", Utils.formatDate(simulationLog.getStartTime()));
    }
    public HashMap<String, Integer[]> getEntitiesBeforeAndAfter(String uuid) {
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
