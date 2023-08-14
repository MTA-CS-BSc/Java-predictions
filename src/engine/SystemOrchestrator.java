package engine;

import engine.consts.Restrictions;
import engine.history.HistoryManager;
import engine.prototypes.implemented.World;
import engine.simulation.SingleSimulation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SystemOrchestrator {
    protected HistoryManager historyManager;
    public SystemOrchestrator() {
        historyManager = new HistoryManager();
    }
    public boolean isHistoryEmpty() {
        return historyManager.isEmpty();
    }
    public boolean writeHistoryToFile() {
        try {
            FileOutputStream f = new FileOutputStream(Restrictions.HISTORY_FILE_PATH);
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(historyManager);
            o.close();
            f.close();
            return true;
//            EngineLoggers.OrchestratorLogger.info("History saved successfully.");
        } catch (Exception e) {
            return false;
//            UILoggers.OrchestratorLogger.info("Could not save history: " + e.getMessage());
        }
    }
    public boolean loadHistory() {
        try {
            FileInputStream fi = new FileInputStream(Restrictions.HISTORY_FILE_PATH);
            ObjectInputStream oi = new ObjectInputStream(fi);
            historyManager = (HistoryManager) oi.readObject();
            return true;
        }

        catch (Exception e) {
            return false;
            // UILoggers.OrchestratorLogger.info("Attempted to load history but no history file was found");
        }
    }
    public void setInitialXmlWorld(World _initialWorld) {
        historyManager.setInitialXmlWorld(_initialWorld);
    }
    public boolean isXmlLoaded() {
        return historyManager.isXmlLoaded();
    }
    public String createSimulation() {
        SingleSimulation sm = new SingleSimulation(historyManager.getInitialWorld());
        historyManager.addPastSimulation(sm);
        return sm.getUUID();
    }
    public void runSimulation(String uuid) throws Exception {
        historyManager.getPastSimulation(uuid).run();
    }
}
