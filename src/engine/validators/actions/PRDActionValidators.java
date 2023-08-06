package engine.validators.actions;

import engine.consts.ActionTypes;
import engine.consts.ConditionSingularities;
import engine.consts.PropTypes;
import engine.logs.Loggers;
import engine.modules.*;
import engine.parsers.ExpressionParser;
import engine.parsers.ValidationExpressionParser;
import engine.prototypes.jaxb.*;

import java.util.List;
import java.util.Objects;

public class PRDActionValidators {
    public static boolean validateAction(PRDWorld world, PRDAction action) {
        if (Objects.isNull(ValidatorsUtils.findPRDEntityByName(world, action.getEntity()))) {
            Loggers.XML_ERRORS_LOGGER.info(String.format("On [%s] action: Entity [%s] does not exist",
                    action.getType(), action.getEntity()));

            return false;
        }

        switch (action.getType()) {
            case ActionTypes.SET:
                return validateSetAction(world, action);
            case ActionTypes.INCREASE:
            case ActionTypes.DECREASE:
                return validateIncreaseDecreaseAction(world, action);
            case ActionTypes.CALCULATION:
                return validateCalculationAction(world, action);
            case ActionTypes.CONDITION:
                return validateConditionAction(world, action, action.getPRDCondition());
            case ActionTypes.KILL:
            case ActionTypes.REPLACE:
            case ActionTypes.PROXIMITY:
                return true;
        }

        return false;
    }
    public static boolean validateIncreaseDecreaseAction(PRDWorld world, PRDAction action) {
        String val = ValidationExpressionParser.parseExpression(world, action, action.getBy());
        String parsedValType = ValidationExpressionParser.getExpressionType(world, action, val);

        if (!PropTypes.NUMERIC_PROPS.contains(parsedValType)) {
            Loggers.XML_ERRORS_LOGGER.info(String.format("On [%s] action, by value [%s] is not numeric",
                    action.getType(), action.getBy()));
            return false;
        }

        return true;
    }
    public static boolean validateSetAction(PRDWorld world, PRDAction action) {
        PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), action.getProperty());

        if (Objects.isNull(property))
            return false;

        String propertyType = property.getType();
        String parsedExpression = ValidationExpressionParser.parseExpression(world, action, action.getValue());
        String parsedExpressionType = ValidationExpressionParser.getExpressionType(world, action, parsedExpression);

        if (PropTypes.NUMERIC_PROPS.contains(propertyType)) {
            if (!PropTypes.NUMERIC_PROPS.contains(parsedExpressionType)) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], value [%s] is not numeric",
                        action.getType(), action.getValue()));
                return false;
            }

            return true;

        }

        else if (PropTypes.BOOLEAN_PROPS.contains(propertyType)) {
            if (PropTypes.BOOLEAN_PROPS.contains(parsedExpressionType)) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], value [%s] is not boolean",
                        action.getType(), action.getValue()));
                return false;
            }

            return true;
        }

        return true;
    }
    private static boolean validateCalculationArgs(PRDWorld world, PRDAction action,
                                                   String arg1, String arg2) {
        String arg1Type = ValidationExpressionParser.getExpressionType(world, action, arg1);
        String arg2Type = ValidationExpressionParser.getExpressionType(world, action, arg2);

        if (!PropTypes.NUMERIC_PROPS.contains(arg1Type)
                || !PropTypes.NUMERIC_PROPS.contains(arg2Type)) {
            Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], one or more of the properties" +
                    "has incorrect type", action.getType()));

            return false;
        }

        return true;
    }
    public static boolean validateCalculationAction(PRDWorld world, PRDAction action) {
        PRDMultiply multiply = action.getPRDMultiply();
        PRDDivide divide = action.getPRDDivide();
        String arg1 = ValidationExpressionParser.parseExpression(world, action,
                Objects.isNull(multiply) ? divide.getArg1() : multiply.getArg1());

        String arg2 = ValidationExpressionParser.parseExpression(world, action,
                Objects.isNull(multiply) ? divide.getArg2() : multiply.getArg2());

        return validateCalculationArgs(world, action, arg1, arg2);
    }
    public static boolean validateSingleCondition(PRDWorld world, PRDAction action,
                                                  PRDCondition condition) {
        PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, condition.getEntity(),
                condition.getProperty());

        if (Objects.isNull(property) || Objects.isNull(action.getPRDThen()))
            return false;

        String propertyType = property.getType();
        String parsedValue = ValidationExpressionParser.parseExpression(world, action, condition.getValue());
        String parsedValueType = ValidationExpressionParser.getExpressionType(world, action, parsedValue);

        if (PropTypes.NUMERIC_PROPS.contains(propertyType)) {
            if (!PropTypes.NUMERIC_PROPS.contains(parsedValueType)) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], condition " +
                        "value has incorrect type", action.getType()));

                return false;
            }

            return true;
        }

        else if (PropTypes.BOOLEAN_PROPS.contains(propertyType)) {
            if (!PropTypes.BOOLEAN_PROPS.contains(parsedValueType)) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], condition " +
                        "value has incorrect type", action.getType()));

                return false;
            }

            return true;
        }

        return true;
    }
    public static boolean validateMultipleCondition(PRDWorld world, PRDAction action,
                                                    PRDCondition condition) {
        List<PRDCondition> allConditions = condition.getPRDCondition();

        for (PRDCondition current : allConditions)
            if (!validateConditionAction(world, action, current))
                return false;

        return true;
    }
    public static boolean validateConditionAction(PRDWorld world, PRDAction action,
                                                  PRDCondition condition) {
        if (condition.getSingularity().equals(ConditionSingularities.SINGLE))
            return validateSingleCondition(world, action, condition);

        return validateMultipleCondition(world, action, condition);
    }
}
