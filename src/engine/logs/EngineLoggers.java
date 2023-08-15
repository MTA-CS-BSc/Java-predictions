package engine.logs;

import engine.EngineAPI;
import engine.parsers.XmlParser;
import engine.simulation.SingleSimulation;

import java.util.logging.Logger;

public abstract class EngineLoggers {
    public static final Logger XML_ERRORS_LOGGER = Logger.getLogger(XmlParser.class.getSimpleName());
    public static final Logger SIMULATION_LOGGER = Logger.getLogger(SingleSimulation.class.getSimpleName());
    public static final Logger API_LOGGER = Logger.getLogger(EngineAPI.class.getSimpleName());
}
