package engine.validators;

import engine.exceptions.TerminationNotFoundException;
import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.entities.PRDEntitiesValidators;
import engine.validators.env.PRDEvironmentValidators;
import engine.validators.rules.PRDRulesValidators;
import engine.validators.termination.PRDTerminationValidators;

import java.util.Objects;

public class PRDWorldValidators {
    public static boolean validateWorld(PRDWorld world) {
        return validateEntities(world)
                && validateTermination(world)
                && validateEnvironment(world)
                && validateRules(world);
    }
    private static boolean validateEntities(PRDWorld world) {
        return PRDEntitiesValidators.validateEntities(world.getPRDEntities());
    }
    private static boolean validateTermination(PRDWorld world) {
        try {
            PRDTerminationValidators.validateStopConditionExists(world.getPRDTermination());
            return true;
        }

        catch (TerminationNotFoundException e) {
            Loggers.XML_ERRORS_LOGGER.info(e.getMessage());
            return false;
        }
    }
    private static boolean validateEnvironment(PRDWorld world) {
        return PRDEvironmentValidators.validateEnvironment(world.getPRDEvironment());
    }
    private static boolean validateRules(PRDWorld world) {
        return PRDRulesValidators.validateRules(world, world.getPRDRules());
    }
}
