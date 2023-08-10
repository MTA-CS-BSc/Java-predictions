package engine.validators.actions;

import engine.consts.ConditionSingularities;
import engine.consts.PropTypes;
import engine.exceptions.InvalidTypeException;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.ValidatorsUtils;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDCondition;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;

import java.util.List;
import java.util.Objects;

public abstract class ConditionValidators {
    private static boolean validateSingleCondition(PRDWorld world, PRDAction action,
                                                  PRDCondition condition) throws Exception {
        PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, condition.getEntity(),
                condition.getProperty());

        PRDProperty floatTestProp = new PRDProperty();
        floatTestProp.setType(PropTypes.FLOAT);

        if (Objects.isNull(property))
            throw new PropertyNotFoundException(String.format("Action [%s]: Entity [%s]: Property [%s] does not exist",
                    action.getType(), action.getEntity(), action.getProperty()));

        if (Objects.isNull(action.getPRDThen()))
            throw new Exception(String.format("Action [%s]: Entity [%s]: No PRDThen tag found",
                    action.getType(), action.getEntity()));

        if (!ValidatorsUtils.validateExpressionType(world, action, property, condition.getValue())
        && !ValidatorsUtils.validateExpressionType(world, action, floatTestProp, condition.getValue()))
            throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: Arithmetic operation must receive arithmetic args",
                    action.getType(), action.getEntity()));

        if (!Objects.isNull(action.getPRDElse()))
            for (PRDAction elseAct : action.getPRDElse().getPRDAction())
                PRDActionValidators.validateAction(world, elseAct);

        if (!Objects.isNull(action.getPRDThen()))
            for (PRDAction thenAct : action.getPRDThen().getPRDAction())
                PRDActionValidators.validateAction(world, thenAct);

        return true;
    }
    private static boolean validateMultipleCondition(PRDWorld world, PRDAction action,
                                                    PRDCondition condition) throws Exception{
        List<PRDCondition> allConditions = condition.getPRDCondition();

        for (PRDCondition current : allConditions)
            if (!validate(world, action, current))
                return false;

        return true;
    }
    public static boolean validate(PRDWorld world, PRDAction action,
                                   PRDCondition condition) throws Exception {
        if (condition.getSingularity().equals(ConditionSingularities.SINGLE))
            return validateSingleCondition(world, action, condition);

        return validateMultipleCondition(world, action, condition);
    }
}
