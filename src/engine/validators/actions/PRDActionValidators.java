package engine.validators.actions;

import engine.logs.Loggers;
import engine.modules.*;
import engine.parsers.ExpressionParser;
import engine.prototypes.jaxb.*;
import engine.validators.PRDWorldValidators;

import java.util.List;
import java.util.Objects;

public class PRDActionValidators {
    public static boolean validateAction(PRDWorld world, PRDAction action) {
        if (!PRDWorldValidators.validateEntityExists(world, action.getEntity())) {
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
    public static boolean validateExpressionIsBoolean(String expression) {
        return expression.equals(BoolPropValues.TRUE) || expression.equals(BoolPropValues.FALSE);
    }
    public static boolean validateExpressionIsNumeric(String expression) {
        return Utils.isDecimal(expression);
    }
    public static boolean validateIncreaseDecreaseAction(PRDWorld world, PRDAction action) {
        String val = ExpressionParser.evaluateExpression(world, action, action.getBy());
        if (!Utils.isDecimal(val)) {
            Loggers.XML_ERRORS_LOGGER.info(String.format("On [%s] action, by value [%s] is not numeric",
                                                        action.getType(), action.getBy()));
            return false;
        }

        return true;
    }
    public static boolean validateSetAction(PRDWorld world, PRDAction action) {
        PRDProperty property = (PRDProperty) Utils.findPropertyByName(world, action.getEntity(), action.getProperty());

        if (Objects.isNull(property))
            return false;

        String propertyType = property.getType();
        String parsedExpression = ExpressionParser.evaluateExpression(world, action, action.getValue());

        if (PropTypes.NUMERIC_PROPS.contains(propertyType)) {
            if (!Utils.isDecimal(parsedExpression)) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], value [%s] is not numeric",
                        action.getType(), action.getValue()));
                return false;
            }

            return true;

        }

        else if (PropTypes.BOOLEAN_PROPS.contains(propertyType)) {
            if (!validateExpressionIsBoolean(parsedExpression)) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], value [%s] is not boolean",
                        action.getType(), action.getValue()));
                return false;
            }

            return true;
        }

        return true;
    }
    private static boolean validateCalculationArgs(String arg1, String arg2, PRDAction action) {
        if (!Utils.isDecimal(arg1) || !Utils.isDecimal(arg2)) {
            Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], one or more of the properties" +
                    "has incorrect type", action.getType()));

            return false;
        }

        return true;
    }
    public static boolean validateCalculationAction(PRDWorld world, PRDAction action) {
        PRDMultiply multiply = action.getPRDMultiply();
        PRDDivide divide = action.getPRDDivide();
        String arg1, arg2;

        if (Objects.isNull(multiply)) {
            arg1 = ExpressionParser.evaluateExpression(world, action, divide.getArg1());
            arg2 = ExpressionParser.evaluateExpression(world, action, divide.getArg2());
        }

        else {
            arg1 = ExpressionParser.evaluateExpression(world, action, multiply.getArg1());
            arg2 = ExpressionParser.evaluateExpression(world, action, multiply.getArg2());
        }

        return validateCalculationArgs(arg1, arg2, action);
    }
    public static boolean validateSingleCondition(PRDWorld world, PRDAction action,
                                                  PRDCondition condition) {
        PRDProperty property = (PRDProperty) Utils.findPropertyByName(world, condition.getEntity(),
                condition.getProperty());

        if (Objects.isNull(property) || Objects.isNull(action.getPRDThen()))
            return false;

        String propertyType = property.getType();
        String parsedValue = ExpressionParser.evaluateExpression(world, action, condition.getValue());

        if (PropTypes.NUMERIC_PROPS.contains(propertyType)) {
            if (!Utils.isDecimal(parsedValue)) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], condition " +
                        "value has incorrect type", action.getType()));

                return false;
            }

            return true;
        }


        else if (PropTypes.BOOLEAN_PROPS.contains(propertyType)) {
            if (!validateExpressionIsBoolean(parsedValue)) {
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