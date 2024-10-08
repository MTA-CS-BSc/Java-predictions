package validators.actions;

import exceptions.EmptyExpressionException;
import exceptions.EntityNotFoundException;
import exceptions.InvalidTypeException;
import modules.ValidatorsUtils;
import prototypes.prd.generated.PRDAction;
import prototypes.prd.generated.PRDActions;
import prototypes.prd.generated.PRDWorld;
import types.PropTypes;

import java.util.Objects;

public abstract class ProximityValidators {
    public static boolean validate(PRDWorld world, PRDAction action) throws Exception {
        PRDAction.PRDBetween prdBetween = action.getPRDBetween();
        PRDAction.PRDEnvDepth prdEnvDepth = action.getPRDEnvDepth();
        PRDActions prdActions = action.getPRDActions();

        if (!PRDActionsValidators.validateActions(world, prdActions.getPRDAction()))
            throw new Exception(String.format("Action [%s]: Could not validate actions", action.getType()));

        else if (Objects.isNull(prdBetween))
            throw new EmptyExpressionException(String.format("Action [%s]: Between does not exist", action.getType()));

        else if (Objects.isNull(ValidatorsUtils.findPRDEntityByName(world, prdBetween.getSourceEntity())))
            throw new EntityNotFoundException(String.format("Action [%s]: Between: Source entity [%s] does not exist",
                    action.getType(), prdBetween.getSourceEntity()));

        else if (Objects.isNull(ValidatorsUtils.findPRDEntityByName(world, prdBetween.getTargetEntity())))
            throw new EntityNotFoundException(String.format("Action [%s]: Between: Target entity [%s] does not exist",
                    action.getType(), prdBetween.getTargetEntity()));

        else if (!ValidatorsUtils.getExpressionType(world, action, prdEnvDepth.getOf()).equals(PropTypes.DECIMAL))
            throw new InvalidTypeException(String.format("Action [%s]: Env Depth: Expression is not decimal",
                    action.getType()));

        return true;
    }
}
