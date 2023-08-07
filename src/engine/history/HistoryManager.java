package engine.history;

import engine.simulation.SingleSimulationLog;

import java.util.HashMap;

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

    //TODO: Implement requested funcs
}
