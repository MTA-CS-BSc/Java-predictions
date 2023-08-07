package engine.validators.termination;

import engine.exceptions.TerminationNotFoundException;
import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDTermination;

public class PRDTerminationValidators {
    public static boolean validateStopConditionExists(PRDTermination termination) throws TerminationNotFoundException {
        if (termination.getPRDByTicksOrPRDBySecond().isEmpty())
            throw new TerminationNotFoundException("No termination found");

        return true;
    }
}
