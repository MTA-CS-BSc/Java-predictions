package engine.validators.actions;

import engine.consts.ConditionSingularities;
import engine.consts.PropTypes;
import engine.exceptions.InvalidTypeException;
import engine.exceptions.PRDThenNotFoundException;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.ValidatorsUtils;
import engine.parsers.ValidationExpressionParser;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDCondition;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;

import java.util.List;
import java.util.Objects;

public class ConditionValidator {

    public static boolean validateSingleCondition(PRDWorld world, PRDAction action,
                                                  PRDCondition condition) throws PRDThenNotFoundException, PropertyNotFoundException, InvalidTypeException {
        PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, condition.getEntity(),
                condition.getProperty());

        if (Objects.isNull(property))
            throw new PropertyNotFoundException(String.format("Action [%s]: Entity [%s]: Property [%s] does not exist",
                    action.getType(), action.getEntity(), action.getProperty()));

        if (Objects.isNull(action.getPRDThen()))
            throw new PRDThenNotFoundException(String.format("Action [%s]: Entity [%s]: No PRDThen tag found",
                    action.getType(), action.getEntity()));

        String propertyType = property.getType();
        String parsedValue = ValidationExpressionParser.parseExpression(world, action, condition.getValue());
        String parsedValueType = ValidationExpressionParser.getExpressionType(world, action, parsedValue);

        if (PropTypes.NUMERIC_PROPS.contains(propertyType)) {
            if (!PropTypes.NUMERIC_PROPS.contains(parsedValueType))
                throw new InvalidTypeException(String.format("Action [%s]: condition " +
                        "value has incorrect type", action.getType()));

            return true;
        }

        else if (PropTypes.BOOLEAN_PROPS.contains(propertyType)) {
            if (!PropTypes.BOOLEAN_PROPS.contains(parsedValueType))
                throw new InvalidTypeException(String.format("Action [%s]: condition " +
                        "value has incorrect type", action.getType()));

            return true;
        }

        return true;
    }
    public static boolean validateMultipleCondition(PRDWorld world, PRDAction action,
                                                    PRDCondition condition) throws PropertyNotFoundException, PRDThenNotFoundException, InvalidTypeException {
        List<PRDCondition> allConditions = condition.getPRDCondition();

        for (PRDCondition current : allConditions)
            if (!validate(world, action, current))
                return false;

        return true;
    }
    public static boolean validate(PRDWorld world, PRDAction action,
                                                  PRDCondition condition) throws PropertyNotFoundException, PRDThenNotFoundException, InvalidTypeException {
        if (condition.getSingularity().equals(ConditionSingularities.SINGLE))
            return validateSingleCondition(world, action, condition);

        return validateMultipleCondition(world, action, condition);
    }
}
