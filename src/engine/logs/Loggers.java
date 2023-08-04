package engine.logs;

import engine.parsers.XmlParser;

import java.util.logging.Logger;

public class Loggers {
    public static final Logger ENGINE_LOGGER = Logger.getLogger(Loggers.class.getSimpleName());
    public static final Logger XML_ERRORS_LOGGER = Logger.getLogger(XmlParser.class.getSimpleName());

}
