package engine.validators.actions;

import engine.logs.Loggers;
import engine.modules.ActionTypes;
import engine.modules.Constants;
import engine.modules.SystemFunctions;
import engine.prototypes.jaxb.*;
import engine.validators.Utils;

import java.util.Arrays;
import java.util.Objects;

public class PRDActionValidators {
    public static boolean validateAction(PRDWorld world, PRDAction action) {
        String type = action.getType();

        if (type.equalsIgnoreCase(ActionTypes.INCREASE)
                || type.equalsIgnoreCase(ActionTypes.DECREASE))
            return validateIncreaseOrDecreaseAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.CALCULATION))
            return validateCalculationAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.SET))
            return validateSetAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.CONDITION))
            return validateConditionAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.KILL))
            return validateKillAction(world, action);

        return false;
    }

    //#region Helpers
    public static boolean isSystemFunction(String type) {
        return Arrays.asList(SystemFunctions.RANDOM, SystemFunctions.ENVIRONMENT).contains(type);
    }
    private static boolean validateRandomProps(String props) {
        return Utils.isDecimal(Arrays.asList(props.split(",")).get(0));
    }
    private static boolean validateEnvProps(PRDWorld world, String props) {
        return world.getPRDEvironment().getPRDEnvProperty()
                .stream()
                .anyMatch(property -> property.getPRDName().equals(Arrays.asList(props.split(",")).get(0)));
    }
    public static boolean handleSystemFunction(PRDWorld world, String props,
                                               String actionType, String type) {
        switch (type) {
            case SystemFunctions.RANDOM:
                return validateRandomProps(props) && ActionTypes.NUMERIC_ACTIONS.contains(actionType);

            case SystemFunctions.ENVIRONMENT:
                return validateEnvProps(world, props);
        }

        return false;
    }

    private static boolean validatePropType(PRDWorld world, String entityName, String propName,
                                            String actionType) {
        PRDProperty prop = (PRDProperty) Utils.findPropertyByName(world, entityName, propName);

        if (Objects.isNull(prop))
            return false;

        if (ActionTypes.NUMERIC_ACTIONS.contains(actionType))
            return prop.getType().equalsIgnoreCase("decimal")
                        || prop.getType().equalsIgnoreCase("float");

        else if (ActionTypes.BOOLEAN_ACTIONS.contains(actionType))
            return prop.getType().equalsIgnoreCase("boolean");

        return true;
    }

    private static boolean validateFreeTextType(PRDWorld world, PRDAction action, String expression) {
        if (ActionTypes.NUMERIC_ACTIONS.contains(action.getType())) {
            try {
                Float.parseFloat(expression);
                return true;
            }

            catch (Exception e) {
                return false;
            }
        }

        else if (ActionTypes.BOOLEAN_ACTIONS.contains(action.getType()))
            // change here
            return true;

        return true;
    }
    private static boolean validateExpression(PRDWorld world,
                                              PRDAction action,
                                              String expression) {
        if (expression.contains("(")) {
            String type = expression.substring(0, expression.indexOf("("));
            String props = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

            if (isSystemFunction(type))
                return handleSystemFunction(world, props, action.getType(), type);
        }

        else if (validatePropExists(world, action.getEntity(), expression))
            return validatePropType(world, action.getEntity(), expression, action.getType());

        return validateFreeTextType(world, action, expression);
    }
    private static boolean validateEntityExists(PRDWorld world, String entityName) {
        boolean flag = world.getPRDEntities().getPRDEntity()
                .stream()
                .anyMatch(element -> element.getName().equals(entityName));

        if (!flag)
            Loggers.XML_ERRORS_LOGGER.info(String.format("Action requested entity name [%s] which doesn't exist", entityName));

        return flag;
    }
    private static boolean validateCalculation(PRDWorld world, PRDAction action) {
        PRDMultiply multiply = action.getPRDMultiply();

        if (Objects.isNull(multiply)) {
            String divide_arg1 = action.getPRDDivide().getArg1();
            String divide_arg2 = action.getPRDDivide().getArg2();

            return validateExpression(world, action, divide_arg1)
                    && validateExpression(world, action, divide_arg2);
        }

        else {
            String multiply_arg1 = action.getPRDMultiply().getArg1();
            String multiply_arg2 = action.getPRDMultiply().getArg2();

            return validateExpression(world, action, multiply_arg1)
                    && validateExpression(world, action, multiply_arg2);
        }
    }
    private static boolean validatePropExists(PRDWorld world, String entityName, String propertyName) {
        PRDEntity foundEntity = (PRDEntity)Utils.findEntityByName(world, entityName);

        if (Objects.isNull(foundEntity)) {
            Loggers.XML_ERRORS_LOGGER.info(String.format("Entity [%s] not found", entityName));
            return false;
        }

        PRDProperty props = (PRDProperty) Utils.findPropertyByName(world, entityName, propertyName);

        if (Objects.isNull(props)) {
            Loggers.XML_ERRORS_LOGGER.info(String.format("Action requested prop name [%s] which does not exist",
                    propertyName));

            return false;
        }

        return true;
    }
    //#endregion

    //#region Actions
    private static boolean validateIncreaseOrDecreaseAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity())
                && validatePropExists(world, action.getEntity(), action.getProperty())
                && validateExpression(world, action, action.getBy());
    }
    private static boolean validateKillAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity());
    }
    private static boolean validateSetAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity())
                && validatePropExists(world, action.getEntity(), action.getProperty())
                && validateExpression(world, action, action.getValue());
    }
    private static boolean validateCalculationAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity())
                && validateCalculation(world, action);
    }
    private static boolean validateConditionAction(PRDWorld world, PRDAction action) {
        if (action.getPRDCondition().getSingularity().equals("single"))
            return validateSingleCondition(world, action, action.getPRDCondition());

        return validateMultipleCondition(world, action.getEntity(), action, action.getPRDCondition());
    }
    public static boolean validateValidOperator(String operator) {
        return Constants.CONDITION_ALLOWED_OPERATORS.contains(operator);
    }
    private static boolean validateSingleCondition(PRDWorld world,
                                                   PRDAction action,
                                                   PRDCondition condition) {
        return  validateValidOperator(condition.getOperator())
                && validateExpression(world, action, condition.getValue());
    }
    private static boolean validateMultipleCondition(PRDWorld world, String entityName,
                                                     PRDAction action,
                                                     PRDCondition condition) {
        boolean result = true;

        for (PRDCondition current : condition.getPRDCondition()) {
            if (current.getSingularity().equals("single"))
                result = result && validateSingleCondition(world, action, current);

            else
                result = result && validateMultipleCondition(world, entityName, action, current);
        }

        return result;
    }
    //#endregion
}
