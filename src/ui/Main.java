package ui;

import ui.handlers.Orchestrator;
import ui.logs.UILoggers;

public class Main {
    public static void main(String[] args) {
        Orchestrator orchestrator = new Orchestrator();

        try {
            orchestrator.start();
        }

        catch (Exception e) {
            UILoggers.OrchestratorLogger.info(e.getMessage());
        }
    }
}
