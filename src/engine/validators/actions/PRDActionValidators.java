package engine.validators.actions;

import engine.modules.ActionTypes;
import engine.modules.ExpressionParser;
import engine.modules.PropTypes;
import engine.prototypes.implemented.Property;
import engine.prototypes.jaxb.*;
import engine.validators.Utils;

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
                return true;
        }

        return false;
    }

    public static boolean validateExpressionIsBoolean(Object expression) {
        String type = "";

        if (Objects.isNull(expression))
            return false;

        if (expression.getClass() == PRDEnvProperty.class)
            type = ((PRDEnvProperty)expression).getType();

        else if (expression.getClass() == PRDProperty.class)
            type = ((PRDProperty)expression).getType();

        else if (expression.getClass() == Property.class)
            type = ((Property)expression).getType();

        else if (expression.getClass() == Integer.class)
            return true;

        if (!type.isEmpty())
            return PropTypes.BOOLEAN_PROPS.contains(type);

        try {
            Boolean.parseBoolean(String.valueOf(expression));
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }
    public static boolean validateExpressionIsNumeric(Object expression) {
        String type = "";

        if (Objects.isNull(expression))
            return false;

        if (expression.getClass() == PRDEnvProperty.class)
            type = ((PRDEnvProperty)expression).getType();

        else if (expression.getClass() == PRDProperty.class)
            type = ((PRDProperty)expression).getType();

        else if (expression.getClass() == Property.class)
            type = ((Property)expression).getType();

        else if (expression.getClass() == Integer.class)
            return true;

        if (!type.isEmpty())
            return PropTypes.NUMERIC_PROPS.contains(type);

        try {
            Float.parseFloat(String.valueOf(expression));
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }
    public static boolean validateEntityExists(PRDWorld world, String entityName) {
        return !Objects.isNull(Utils.findEntityByName(world, entityName));
    }
    public static boolean validateKillAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity());
    }
    public static boolean validateIncreaseDecreaseAction(PRDWorld world, PRDAction action) {
        return validateExpressionIsNumeric(ExpressionParser.parseExpression(world, action.getEntity(), action.getBy()));
    }
    public static boolean validateSetAction(PRDWorld world, PRDAction action) {
        PRDProperty property = (PRDProperty) Utils.findPropertyByName(world, action.getEntity(), action.getProperty());

        if (!validateEntityExists(world, action.getEntity()) || Objects.isNull(property))
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
}