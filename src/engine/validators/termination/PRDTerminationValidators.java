package engine.validators.termination;

import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public abstract class PRDTerminationValidators {
    public static boolean validateStopConditionExists(PRDWorld world) throws Exception {
        if (world.getPRDTermination().getPRDBySecondOrPRDByTicks().isEmpty()
                && Objects.isNull(world.getPRDTermination().getPRDByUser()))
            throw new Exception("No termination found");

        else if (!world.getPRDTermination().getPRDBySecondOrPRDByTicks().isEmpty()
                && !Objects.isNull(world.getPRDTermination().getPRDByUser()))
            throw new Exception("Termination by user cannot come along with any other termination rule.");

        return true;
    }
}