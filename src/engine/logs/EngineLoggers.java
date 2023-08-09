package engine.logs;

import engine.parsers.XmlParser;
import engine.simulation.SingleSimulation;

import java.util.logging.Logger;

public class EngineLoggers {
    public static final Logger XML_ERRORS_LOGGER = Logger.getLogger(XmlParser.class.getSimpleName());
    public static final Logger SIMULATION_LOGGER = Logger.getLogger(SingleSimulation.class.getSimpleName());
}
