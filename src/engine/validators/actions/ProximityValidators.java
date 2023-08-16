package engine.validators.actions;

import engine.consts.PropTypes;
import engine.exceptions.EmptyExpressionException;
import engine.exceptions.EntityNotFoundException;
import engine.exceptions.InvalidTypeException;
import engine.modules.ValidatorsUtils;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDActions;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public abstract class ProximityValidators {
    public static boolean validate(PRDWorld world, PRDAction action) throws Exception {
        PRDAction.PRDBetween prdBetween = action.getPRDBetween();
        PRDAction.PRDEnvDepth prdEnvDepth = action.getPRDEnvDepth();
        PRDActions prdActions = action.getPRDActions();
        PRDProperty decimalTestProp = new PRDProperty();
        decimalTestProp.setType(PropTypes.DECIMAL);

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

        else if (ValidatorsUtils.validateExpressionType(world, action, decimalTestProp, prdEnvDepth.getOf()))
            throw new InvalidTypeException(String.format("Action [%s]: Env Depth: Expression is not decimal",
                    action.getType()));

        return true;
    }
}
