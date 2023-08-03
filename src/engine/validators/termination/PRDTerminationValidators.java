package engine.validators.termination;

import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDTermination;

public class PRDTerminationValidators {
    public static boolean validateStopConditionExists(PRDTermination termination) {
        if (termination.getPRDByTicksOrPRDBySecond().size() == 0) {
            Loggers.XML_ERRORS_LOGGER.trace("No termination found");
            return false;
        }

        return true;
    }
}
