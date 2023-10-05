package validators;

import other.ResponseDTO;
import exceptions.GridSizeException;
import prototypes.prd.generated.PRDWorld;
import validators.entities.PRDEntitiesValidators;
import validators.env.PRDEnvironmentValidators;
import validators.rules.PRDRulesValidators;
import modules.Constants;

public abstract class PRDWorldValidators {
    public static ResponseDTO validateWorld(PRDWorld world) {
        StringTrimmer.trimAllStrings(world);

        try {
            if (validateEntities(world) && validateEnvironment(world) &&
                    validateRules(world) && validateGrid(world))
                return new ResponseDTO(Constants.API_RESPONSE_OK, "XML validation passed");

            return new ResponseDTO(Constants.API_RESPONSE_SERVER_ERROR, "XML validation failed", "Unknown");
        } catch (Exception e) {
            return new ResponseDTO(Constants.API_RESPONSE_BAD_REQUEST, "XML validation failed", e.getMessage());
        }
    }

    private static boolean validateEntities(PRDWorld world) throws Exception {
        return PRDEntitiesValidators.validateEntities(world);
    }

    private static boolean validateEnvironment(PRDWorld world) throws Exception {
        return PRDEnvironmentValidators.validateEnvironment(world);
    }

    private static boolean validateRules(PRDWorld world) throws Exception {
        return PRDRulesValidators.validateRules(world);
    }

    private static boolean validateGrid(PRDWorld world) throws Exception {
        PRDWorld.PRDGrid grid = world.getPRDGrid();

        if (grid.getRows() < Constants.MIN_GRID_LINEAR_SIZE || grid.getRows() > Constants.MAX_GRID_LINEAR_SIZE)
            throw new GridSizeException("World grid rows range is [10, 100] but value is " + grid.getRows());

        if (grid.getColumns() < Constants.MIN_GRID_LINEAR_SIZE || grid.getColumns() > Constants.MAX_GRID_LINEAR_SIZE)
            throw new GridSizeException("World grid columns range is [10, 100] but value is " + grid.getColumns());

        return true;

    }
}