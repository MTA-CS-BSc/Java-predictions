package engine.validators;

import dtos.ResponseDTO;
import engine.exceptions.GridSizeException;
import engine.exceptions.ValueNotInRangeException;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.entities.PRDEntitiesValidators;
import engine.validators.env.PRDEnvironmentValidators;
import engine.validators.rules.PRDRulesValidators;
import engine.validators.termination.PRDTerminationValidators;

public abstract class PRDWorldValidators {
    public static ResponseDTO validateWorld(PRDWorld world) {
        StringTrimmer.trimAllStrings(world);

        try {
            if (validateEntities(world) && validateTermination(world)
                    && validateEnvironment(world) && validateRules(world)
                    && validateThreadpool(world) && validateGrid(world))
                return new ResponseDTO(200, "XML validation passed");

            return new ResponseDTO(500, "XML validation failed", "Unknown");
        } catch (Exception e) {
            return new ResponseDTO(400, "XML validation failed", e.getMessage());
        }
    }

    private static boolean validateEntities(PRDWorld world) throws Exception {
        return PRDEntitiesValidators.validateEntities(world);
    }

    private static boolean validateTermination(PRDWorld world) throws Exception {
        return PRDTerminationValidators.validateStopConditionExists(world);
    }

    private static boolean validateEnvironment(PRDWorld world) throws Exception {
        return PRDEnvironmentValidators.validateEnvironment(world);
    }

    private static boolean validateRules(PRDWorld world) throws Exception {
        return PRDRulesValidators.validateRules(world);
    }

    private static boolean validateThreadpool(PRDWorld world) throws Exception {
        if (world.getPRDThreadCount() <= 0)
            throw new ValueNotInRangeException("Threadpool count is less or equal to zero");

        return true;
    }

    private static boolean validateGrid(PRDWorld world) throws Exception {
        PRDWorld.PRDGrid grid = world.getPRDGrid();

        if (grid.getRows() < 10 || grid.getRows() > 100)
            throw new GridSizeException("World grid rows range is [10, 100] but value is " + grid.getRows());

        if (grid.getColumns() < 10 || grid.getColumns() > 100)
            throw new GridSizeException("World grid columns range is [10, 100] but value is " + grid.getColumns());

        return true;

    }
}