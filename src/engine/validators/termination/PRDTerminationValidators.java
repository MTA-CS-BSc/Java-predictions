package engine.validators.termination;

import engine.prototypes.jaxb.PRDTermination;
import engine.prototypes.jaxb.PRDWorld;

public abstract class PRDTerminationValidators {
    public static boolean validateStopConditionExists(PRDWorld world) throws Exception {
        if (world.getPRDTermination().getPRDByTicksOrPRDBySecond().isEmpty())
            throw new Exception("No termination found");

        return true;
    }
}