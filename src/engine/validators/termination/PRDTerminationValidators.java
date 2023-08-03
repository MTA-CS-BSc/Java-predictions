package engine.validators.termination;

import engine.prototypes.jaxb.PRDTermination;

public class PRDTerminationValidators {
    public static boolean validateStopConditionExists(PRDTermination termination) {
        return termination.getPRDByTicksOrPRDBySecond().size() > 0;
    }
}
