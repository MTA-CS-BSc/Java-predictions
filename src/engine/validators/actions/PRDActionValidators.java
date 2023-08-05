package engine.validators.actions;

import engine.logs.Loggers;
import engine.modules.ActionTypes;
import engine.modules.ConditionSingularities;
import engine.modules.ExpressionParser;
import engine.modules.PropTypes;
import engine.prototypes.implemented.Property;
import engine.prototypes.jaxb.*;
import engine.validators.PRDWorldValidators;
import engine.modules.Utils;

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
                return true;
        }

        return false;
    }
    public static boolean validateExpressionIsBoolean(Object parsedExpression) {
        String type = "";

        if (Objects.isNull(parsedExpression))
            return false;

        if (parsedExpression.getClass() == PRDEnvProperty.class)
            type = ((PRDEnvProperty)parsedExpression).getType();

        else if (parsedExpression.getClass() == PRDProperty.class)
            type = ((PRDProperty)parsedExpression).getType();

        else if (parsedExpression.getClass() == Property.class)
            type = ((Property)parsedExpression).getType();

        if (!type.isEmpty())
            return PropTypes.BOOLEAN_PROPS.contains(type);

        try {
            Boolean.parseBoolean(String.valueOf(parsedExpression));
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }
    public static boolean validateExpressionIsNumeric(Object parsedExpression) {
        String type = "";

        if (Objects.isNull(parsedExpression))
            return false;

        if (parsedExpression.getClass() == PRDEnvProperty.class)
            type = ((PRDEnvProperty)parsedExpression).getType();

        else if (parsedExpression.getClass() == PRDProperty.class)
            type = ((PRDProperty)parsedExpression).getType();

        else if (parsedExpression.getClass() == Property.class)
            type = ((Property)parsedExpression).getType();

        else if (parsedExpression.getClass() == Integer.class
                    || parsedExpression.getClass() == Float.class)
            return true;

        if (!type.isEmpty())
            return PropTypes.NUMERIC_PROPS.contains(type);

        try {
            Float.parseFloat(String.valueOf(parsedExpression));
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }
    public static boolean validateIncreaseDecreaseAction(PRDWorld world, PRDAction action) {
        if (!validateExpressionIsNumeric(ExpressionParser.parseExpression(world, action, action.getBy()))) {
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
        Object expression = ExpressionParser.parseExpression(world, action, action.getValue());

        if (PropTypes.NUMERIC_PROPS.contains(propertyType)) {
            if (!validateExpressionIsNumeric(expression)) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], value [%s] is not numeric",
                        action.getType(), action.getValue()));
                return false;
            }

            return true;

        }

        else if (PropTypes.BOOLEAN_PROPS.contains(propertyType)) {
            if (!validateExpressionIsBoolean(expression)) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], value [%s] is not boolean",
                        action.getType(), action.getValue()));
                return false;
            }

            return true;
        }

        return true;
    }
    public static boolean validateCalculationAction(PRDWorld world, PRDAction action) {
        PRDMultiply multiply = action.getPRDMultiply();
        PRDDivide divide = action.getPRDDivide();

        if (Objects.isNull(multiply)) {
            Object arg1 = ExpressionParser.parseExpression(world, action, divide.getArg1());
            Object arg2 = ExpressionParser.parseExpression(world, action, divide.getArg2());

            return validateExpressionIsNumeric(arg1) && validateExpressionIsNumeric(arg2);
        }

        Object arg1 = ExpressionParser.parseExpression(world, action, multiply.getArg1());
        Object arg2 = ExpressionParser.parseExpression(world, action, multiply.getArg2());

        if (!validateExpressionIsNumeric(arg1) || !validateExpressionIsNumeric(arg2)) {
            Loggers.XML_ERRORS_LOGGER.info(String.format("On action [%s], one or more of the properties" +
                    "has incorrect type", action.getType()));

            return false;
        }

        return true;
    }
    public static boolean validateSingleCondition(PRDWorld world, PRDAction action,
                                                  PRDCondition condition) {
        PRDProperty property = (PRDProperty) Utils.findPropertyByName(world, condition.getEntity(),
                condition.getProperty());

        if (Objects.isNull(property) || Objects.isNull(action.getPRDThen()))
            return false;

        String propertyType = property.getType();
        Object parsedValue = ExpressionParser.parseExpression(world, action, condition.getValue());

        if (PropTypes.NUMERIC_PROPS.contains(propertyType)) {
            if (!validateExpressionIsNumeric(parsedValue)) {
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