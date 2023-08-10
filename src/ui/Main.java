package ui;

import ui.handlers.MainMenuHandler;
import ui.handlers.Orchestrator;
import ui.logs.UILoggers;

public class Main {
    public static void main(String[] args) {
        Orchestrator systemOrchestrator = new Orchestrator();
        try {
            systemOrchestrator.start();
        }

        catch (Exception e) {
            UILoggers.OrchestratorLogger.info(e.getMessage());
        }
    }
}
