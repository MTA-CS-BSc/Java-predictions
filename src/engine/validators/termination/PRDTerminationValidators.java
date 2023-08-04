package engine.validators.termination;

import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDTermination;

public class PRDTerminationValidators {
    public static boolean validateStopConditionExists(PRDTermination termination) {
        if (termination.getPRDByTicksOrPRDBySecond().isEmpty()) {
            Loggers.XML_ERRORS_LOGGER.trace("No termination found");
            return false;
        }

        return true;
    }
}
