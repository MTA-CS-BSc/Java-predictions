package engine.validators.termination;

import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public abstract class PRDTerminationValidators {
    public static boolean validateStopConditionExists(PRDWorld world) throws Exception {
        if (world.getPRDTermination().getPRDBySecondOrPRDByTicks().isEmpty()
        && Objects.isNull(world.getPRDTermination().getPRDByUser()))
            throw new Exception("No termination found");

        return true;
    }
}