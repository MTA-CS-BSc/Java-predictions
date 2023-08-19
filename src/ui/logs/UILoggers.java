package ui.logs;

import helpers.CustomConsoleHandler;
import ui.handlers.Orchestrator;

import java.util.Scanner;
import java.util.logging.Logger;

// Using JUL because in this project, a logging framework is a bit of an overkill.
public class UILoggers {
    public static final Logger ScannerLogger = Logger.getLogger(Scanner.class.getSimpleName());
    public static final Logger OrchestratorLogger = Logger.getLogger(Orchestrator.class.getSimpleName());
    public static void formatLogger(Logger logger) {
        logger.setUseParentHandlers(false);
        logger.addHandler(new CustomConsoleHandler());
    }
}
