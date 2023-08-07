package engine.exceptions;

import engine.logs.Loggers;

public class NoXMLException extends Exception {
    public NoXMLException() {
        super("No XML was loaded!");
        Loggers.SIMULATION_LOGGER.info("Simulation run attempted before an XML was loaded");
    }
}
