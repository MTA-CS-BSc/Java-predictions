package engine.validators;

import engine.logs.EngineLoggers;
import engine.prototypes.jaxb.*;
import engine.validators.entities.PRDEntitiesValidators;
import engine.validators.env.PRDEnvironmentValidators;
import engine.validators.rules.PRDRulesValidators;
import engine.validators.termination.PRDTerminationValidators;

public abstract class PRDWorldValidators {
    public static boolean validateWorld(PRDWorld world) {
        StringTrimmer.trimAllStrings(world);

        return validateEntities(world) && validateTermination(world)
                && validateEnvironment(world) && validateRules(world)
                && validateThreadpool(world) && validateGrid(world);
    }
    private static boolean validateEntities(PRDWorld world) {
        try {
            return PRDEntitiesValidators.validateEntities(world);
        }

        catch (Exception e) {
            EngineLoggers.XML_ERRORS_LOGGER.info(e.getMessage());
            return false;
        }
    }
    private static boolean validateTermination(PRDWorld world) {
        try {
            return PRDTerminationValidators.validateStopConditionExists(world);
        }

        catch (Exception e) {
            EngineLoggers.XML_ERRORS_LOGGER.info(e.getMessage());
            return false;
        }
    }
    private static boolean validateEnvironment(PRDWorld world) {
        try {
            return PRDEnvironmentValidators.validateEnvironment(world);
        }
        catch (Exception e) {
            EngineLoggers.XML_ERRORS_LOGGER.info(e.getMessage());
            return false;
        }
    }
    private static boolean validateRules(PRDWorld world) {
        try {
            return PRDRulesValidators.validateRules(world);
        }

        catch (Exception e) {
            EngineLoggers.XML_ERRORS_LOGGER.info(e.getMessage());
            return false;
        }
    }
    private static boolean validateThreadpool(PRDWorld world) {
        if (world.getPRDThreadCount() <= 0)
            EngineLoggers.XML_ERRORS_LOGGER.info("Threadpool count is less or equal to zero");

        return world.getPRDThreadCount() > 0;
    }
    private static boolean validateGrid(PRDWorld world) {
        PRDWorld.PRDGrid grid = world.getPRDGrid();

        if (grid.getRows() < 10 || grid.getRows() > 100) {
            EngineLoggers.XML_ERRORS_LOGGER.info("World grid rows range is [10, 100] but value is " + grid.getRows());
            return false;
        }

        else if (grid.getColumns() < 10 || grid.getColumns() > 100) {
            EngineLoggers.XML_ERRORS_LOGGER.info("World grid columns range is [10, 100] but value is " + grid.getColumns());
            return false;
        }

        return true;

    }
}