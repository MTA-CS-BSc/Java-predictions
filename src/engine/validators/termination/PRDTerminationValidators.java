package engine.validators.termination;

import engine.prototypes.jaxb.PRDTermination;

public class PRDTerminationValidators {
    public static boolean validateStopConditionExists(PRDTermination termination) throws Exception {
        if (termination.getPRDByTicksOrPRDBySecond().isEmpty())
            throw new Exception("No termination found");

        return true;
    }
}
