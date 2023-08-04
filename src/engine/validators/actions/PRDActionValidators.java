package engine.validators.actions;

import engine.logs.Loggers;
import engine.modules.Constants;
import engine.prototypes.jaxb.*;
import engine.validators.Utils;

import java.util.Arrays;
import java.util.Objects;

public class PRDActionValidators {
    public static boolean validateAction(PRDWorld world, PRDAction action) {
        String type = action.getType();

        if (type.equalsIgnoreCase("increase")
                || type.equalsIgnoreCase("decrease"))
            return validateIncreaseOrDecreaseAction(world, action);

        else if (type.equalsIgnoreCase("calculation"))
            return validateCalculationAction(world, action);

        else if (type.equalsIgnoreCase("set"))
            return validateSetAction(world, action);

        else if (type.equalsIgnoreCase("condition"))
            return validateConditionAction(world, action);

        else if (type.equalsIgnoreCase("kill"))
            return validateKillAction(world, action);

        return false;
    }

    //#region Helpers
    private static boolean isSystemFunction(String type) {
        return Arrays.asList("random", "environment").contains(type);
    }
    private static boolean validateRandomProps(String props) {
        return Utils.isDecimal(Arrays.asList(props.split(",")).get(0));
    }
    private static boolean validateEnvProps(PRDWorld world, String props) {
        return world.getPRDEvironment().getPRDEnvProperty()
                .stream()
                .anyMatch(property -> property.getPRDName().equals(Arrays.asList(props.split(",")).get(0)));
    }
    public static boolean handleSystemFunction(PRDWorld world, String props, String type) {
        switch (type) {
            case "random":
                return validateRandomProps(props);
            case "environment":
                return validateEnvProps(world, props);
        }

        return false;
    }
    private static boolean validateExpression(PRDWorld world, String entityName, String expression) {
        if (expression.contains("(")) {
            String type = expression.substring(0, expression.indexOf("("));
            String props = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

            if (isSystemFunction(type)) {
                return handleSystemFunction(world, props, type);
            }
        }

        else if (validatePropExists(world, entityName, expression))
            return true;

        Loggers.XML_ERRORS_LOGGER.info(String.format("Prop [%s] not found", expression));
        // TODO: Validate types
        return true;
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

            return validateExpression(world, action.getEntity(), divide_arg1)
                    && validateExpression(world, action.getEntity(), divide_arg2);
        }

        else {
            String multiply_arg1 = action.getPRDMultiply().getArg1();
            String multiply_arg2 = action.getPRDMultiply().getArg2();

            return validateExpression(world, action.getEntity(), multiply_arg1)
                    && validateExpression(world, action.getEntity(), multiply_arg2);
        }
    }
    private static boolean validatePropExists(PRDWorld world, String entityName, String propertyName) {
        PRDEntity foundEntity = Utils.findEntityByName(world, entityName);

        if (Objects.isNull(foundEntity)) {
            Loggers.XML_ERRORS_LOGGER.info(String.format("Entity [%s] not found", entityName));
            return false;
        }

        PRDProperty props = Utils.findPropertyByName(world, entityName, propertyName);

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
                && validateExpression(world, action.getEntity(), action.getBy());
    }
    private static boolean validateKillAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity());
    }
    private static boolean validateSetAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity())
                && validatePropExists(world, action.getEntity(), action.getProperty())
                && validateExpression(world, action.getEntity(), action.getValue());
    }
    private static boolean validateCalculationAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity())
                && validatePropExists(world, action.getEntity(), action.getResultProp())
                && validateCalculation(world, action);
    }
    private static boolean validateConditionAction(PRDWorld world, PRDAction action) {
        return validateCondition(world, action.getEntity(), action.getPRDCondition());
    }
    public static boolean validateValidOperator(String operator) {
        return Constants.CONDITION_ALLOWED_OPERATORS.contains(operator);
    }
    private static boolean validateSingleCondition(PRDWorld world, String entityName, PRDCondition condition) {
        return validatePropExists(world, entityName, condition.getProperty())
                && validateValidOperator(condition.getOperator())
                && validateExpression(world, entityName, condition.getValue());
    }
    private static boolean validateMultipleCondition(PRDWorld world, String entityName, PRDCondition condition) {
        for (PRDCondition current : condition.getPRDCondition()) {
            if (current.getSingularity().equals("single") &&
                !validateSingleCondition(world, entityName, current))
                return false;

            else {
                if (!validateMultipleCondition(world, entityName, current))
                    return false;
            }
        }

        return true;
    }
    private static boolean validateCondition(PRDWorld world, String entityName, PRDCondition condition) {
        if (condition.getSingularity().equals("single"))
            return validateSingleCondition(world, entityName, condition);

        return validateMultipleCondition(world, entityName, condition);
    }
    //#endregion
}
