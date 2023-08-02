package engine.logs;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

public class Loggers {
    public static final Logger ENGINE_LOGGER = LoggerFactory.getLogger(Loggers.class);

    public static void ValueChangedLog(String context, String variableClassName) {
        Loggers.ENGINE_LOGGER.debug(String.format("[%s]: [%s] changed value", context, variableClassName));
    }

}
