package ui.logs;

import ui.handlers.Orchestrator;
import ui.scanners.MainMenuScanner;

import java.util.Scanner;
import java.util.logging.Logger;

public class UILoggers {
    public static final Logger ScannerLogger = Logger.getLogger(Scanner.class.getSimpleName());
    public static final Logger OrchestratorLogger = Logger.getLogger(Orchestrator.class.getSimpleName());
}
