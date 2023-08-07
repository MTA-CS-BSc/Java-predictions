package engine.validators;

import engine.exceptions.*;
import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.entities.PRDEntitiesValidators;
import engine.validators.env.PRDEvironmentValidators;
import engine.validators.rules.PRDRulesValidators;
import engine.validators.termination.PRDTerminationValidators;

import java.util.Objects;

public class PRDWorldValidators {
    public static boolean validateWorld(PRDWorld world) {
        return validateEntities(world) && validateTermination(world)
                    && validateEnvironment(world) && validateRules(world);
    }
    private static boolean validateEntities(PRDWorld world) {
        try {
            return PRDEntitiesValidators.validateEntities(world.getPRDEntities());
        }

        catch (Exception e) {
            Loggers.XML_ERRORS_LOGGER.info(e.getMessage());
            return false;
        }
    }
    private static boolean validateTermination(PRDWorld world) {
        try {
            return PRDTerminationValidators.validateStopConditionExists(world.getPRDTermination());
        }

        catch (TerminationNotFoundException e) {
            Loggers.XML_ERRORS_LOGGER.info(e.getMessage());
            return false;
        }
    }
    private static boolean validateEnvironment(PRDWorld world) {
        try {
            return PRDEvironmentValidators.validateEnvironment(world.getPRDEvironment());
        }
        catch (Exception e) {
            Loggers.XML_ERRORS_LOGGER.info(e.getMessage());
            return false;
        }
    }
    private static boolean validateRules(PRDWorld world) {
        try {
            return PRDRulesValidators.validateRules(world, world.getPRDRules());
        }

        catch (Exception e) {
            Loggers.XML_ERRORS_LOGGER.info(e.getMessage());
            return false;
        }
    }
}
