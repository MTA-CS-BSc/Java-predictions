package engine.validators.actions;

import engine.modules.ActionTypes;
import engine.modules.ConditionSingularities;
import engine.modules.ExpressionParser;
import engine.modules.PropTypes;
import engine.prototypes.implemented.Property;
import engine.prototypes.jaxb.*;
import engine.validators.PRDWorldValidators;
import engine.validators.Utils;

import java.util.List;
import java.util.Objects;

public class PRDActionValidators {
    public static boolean validateAction(PRDWorld world, PRDAction action) {
        switch (action.getType()) {
            case ActionTypes.KILL:
                return validateKillAction(world, action);
            case ActionTypes.SET:
                return validateSetAction(world, action);
            case ActionTypes.INCREASE:
            case ActionTypes.DECREASE:
                return validateIncreaseDecreaseAction(world, action);
            case ActionTypes.CALCULATION:
                return validateCalculationAction(world, action);
            case ActionTypes.CONDITION:
                return validateConditionAction(world, action, action.getPRDCondition());
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

        else if (parsedExpression.getClass() == Integer.class)
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
    public static boolean validateKillAction(PRDWorld world, PRDAction action) {
        return PRDWorldValidators.validateEntityExists(world, action.getEntity());
    }
    public static boolean validateIncreaseDecreaseAction(PRDWorld world, PRDAction action) {
        return validateExpressionIsNumeric(ExpressionParser.parseExpression(world, action.getEntity(), action.getBy()));
    }
    public static boolean validateSetAction(PRDWorld world, PRDAction action) {
        PRDProperty property = (PRDProperty) Utils.findPropertyByName(world, action.getEntity(), action.getProperty());

        if (!PRDWorldValidators.validateEntityExists(world, action.getEntity()) || Objects.isNull(property))
            return false;

        String propertyType = property.getType();
        Object expression = ExpressionParser.parseExpression(world, action.getEntity(), action.getValue());

        if (PropTypes.NUMERIC_PROPS.contains(propertyType))
            return validateExpressionIsNumeric(expression);

        else if (PropTypes.BOOLEAN_PROPS.contains(propertyType))
            return validateExpressionIsBoolean(expression);

        //else if (propertyType.equals(PropTypes.STRING))
        return true;
    }
    public static boolean validateCalculationAction(PRDWorld world, PRDAction action) {
        PRDMultiply multiply = action.getPRDMultiply();
        PRDDivide divide = action.getPRDDivide();

        if (Objects.isNull(multiply)) {
            Object arg1 = ExpressionParser.parseExpression(world, action.getEntity(), divide.getArg1());
            Object arg2 = ExpressionParser.parseExpression(world, action.getEntity(), divide.getArg2());

            return validateExpressionIsNumeric(arg1) && validateExpressionIsNumeric(arg2);
        }

        Object arg1 = ExpressionParser.parseExpression(world, action.getEntity(), multiply.getArg1());
        Object arg2 = ExpressionParser.parseExpression(world, action.getEntity(), multiply.getArg2());

        return validateExpressionIsNumeric(arg1) && validateExpressionIsNumeric(arg2);
    }
    public static boolean validateSingleCondition(PRDWorld world, PRDAction action,
                                                  PRDCondition condition) {
        PRDProperty property = (PRDProperty) Utils.findPropertyByName(world, condition.getEntity(),
                condition.getProperty());

        if (!PRDWorldValidators.validateEntityExists(world, condition.getEntity())
            || Objects.isNull(property) || Objects.isNull(action.getPRDThen()))
            return false;

        String propertyType = property.getType();
        Object parsedValue = ExpressionParser.parseExpression(world, condition.getEntity(), condition.getValue());

        if (PropTypes.NUMERIC_PROPS.contains(propertyType))
            return validateExpressionIsNumeric(parsedValue);

        else if (PropTypes.BOOLEAN_PROPS.contains(propertyType))
            return validateExpressionIsBoolean(parsedValue);

        //else if (propertyType.equals(PropTypes.STRING))
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
    public static boolean validateConditionAction(PRDWorld world, PRDAction action, PRDCondition condition) {
        if (!PRDWorldValidators.validateEntityExists(world, action.getEntity()))
            return false;

        if (condition.getSingularity().equals(ConditionSingularities.SINGLE))
            return validateSingleCondition(world, action, condition);

        return validateMultipleCondition(world, action, condition);
    }
}