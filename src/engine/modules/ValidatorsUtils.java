package engine.modules;

import engine.consts.PropTypes;
import engine.consts.SystemFunctions;
import engine.exceptions.InvalidTypeException;
import engine.prototypes.jaxb.*;

import java.util.Objects;
import java.util.regex.Pattern;

public class ValidatorsUtils {
    public static PRDEntity findPRDEntityByName(PRDWorld world, String entityName) {
        return world.getPRDEntities().getPRDEntity()
                .stream()
                .filter(element -> element.getName().equals(entityName))
                .findFirst().orElse(null);
    }
    public static PRDProperty findPRDPropertyByName(PRDWorld world, String entityName, String propertyName) {
        PRDEntity entity = findPRDEntityByName(world, entityName);

        if (Objects.isNull(entity))
            return null;

        return entity.getPRDProperties().getPRDProperty()
                .stream()
                .filter(element -> element.getPRDName().equals(propertyName))
                .findFirst().orElse(null);
    }
    public static boolean isSystemFunction(String expression) {
        String pattern = "^[A-Za-z]{1,}\\([A-Za-z0-9]{1,}\\)$";

        if (!Pattern.matches(pattern, expression))
            return false;

        return SystemFunctions.ALL_SYSTEM_FUNCS.contains(expression.substring(0, expression.indexOf("(")));
    }
    public static String getSystemFunctionType(String expression) {
        return expression.substring(0, expression.indexOf("("));
    }
    public static boolean validateExpressionType(PRDWorld world, PRDAction action, PRDProperty property, String expression) {
        PRDProperty expressionEntityProp = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), expression);

        if (ValidatorsUtils.isSystemFunction(expression))
            return validateSystemFuncExpressionType(world, action, property, expression);

        else if (!Objects.isNull(expressionEntityProp))
            return expressionEntityProp.getType().equals(property.getType())
                    || (expressionEntityProp.getType().equals(PropTypes.DECIMAL)
                    && property.getType().equals(PropTypes.FLOAT));

        else {
            if (Utils.isDecimal(expression))
                return PropTypes.NUMERIC_PROPS.contains(property.getType());

            else if (Utils.isFloat(expression)) {
                if (!expression.matches(Utils.REGEX_ONLY_ZEROES_AFTER_DOT))
                    return property.getType().equals(PropTypes.FLOAT);

                return PropTypes.NUMERIC_PROPS.contains(property.getType());
            }

            else if (Utils.isBoolean(expression))
                return property.getType().equals(PropTypes.BOOLEAN);
        }

        return property.getType().equals(PropTypes.STRING);
    }
    private static boolean validateSystemFuncRandom(PRDProperty property, String systemFunctionValue) {
        return property.getType().equals(PropTypes.DECIMAL) && Utils.isDecimal(systemFunctionValue);
    }
    private static boolean validateSystemFuncExpressionType(PRDWorld world, PRDAction action,
                                                            PRDProperty property, String expression) {
        String systemFunctionType = ValidatorsUtils.getSystemFunctionType(expression);
        String systemFunctionValue = expression.substring(expression.lastIndexOf("(") + 1, expression.lastIndexOf(")"));

        switch (systemFunctionType) {
            case SystemFunctions.RANDOM:
                return validateSystemFuncRandom(property, systemFunctionValue);
            case SystemFunctions.ENVIRONMENT:
                return validateSystemFuncEnv(world, property, systemFunctionValue);
            case SystemFunctions.EVALUATE:
                return validateSystemFuncEvaluate(world, action.getEntity(), property, systemFunctionValue);
            case SystemFunctions.PERCENT:
                return validateSystemFuncPercent(world, action, property, systemFunctionValue);
            case SystemFunctions.TICKS:
                return validateSystemFuncTicks(world, action, property, systemFunctionValue);
        }

        return false;
    }
    private static boolean validateSystemFuncEnv(PRDWorld world, PRDProperty property, String systemFunctionValue) {
        PRDEnvProperty envProp = world.getPRDEvironment().getPRDEnvProperty()
                .stream()
                .filter(element -> element.getPRDName().equals(systemFunctionValue))
                .findFirst().orElse(null);

        return (!Objects.isNull(envProp) &&
                (envProp.getType().equals(property.getType())
                || (envProp.getType().equals(PropTypes.DECIMAL) && property.getType().equals(PropTypes.FLOAT))));
    }
    private static boolean validateSystemFuncEvaluate(PRDWorld world, String entityName, PRDProperty property,
                                                      String systemFunctionValue) {
        PRDEntity actionEntity = world.getPRDEntities().getPRDEntity()
                .stream()
                .filter(element -> element.getName().equals(entityName))
                .findFirst().orElse(null);

        if (Objects.isNull(actionEntity))
            return false;

        PRDProperty systemFuncProp = actionEntity.getPRDProperties().getPRDProperty()
                .stream()
                .filter(element -> element.getPRDName().equals(systemFunctionValue))
                .findFirst().orElse(null);

        if (Objects.isNull(systemFuncProp))
            return false;

        return systemFuncProp.getType().equals(property.getType())
                || (systemFuncProp.getType().equals(PropTypes.DECIMAL) && property.getType().equals(PropTypes.FLOAT));
    }
    private static boolean validateSystemFuncPercent(PRDWorld world, PRDAction action, PRDProperty property, String args) {
        String[] splitArgs = args.split(",");

        if (splitArgs.length != 2)
            return false;

        return validateExpressionType(world, action, property, splitArgs[0])
                && validateExpressionType(world, action, property, splitArgs[1]);
    }

    private static boolean validateSystemFuncTicks(PRDWorld world, PRDAction action, PRDProperty property, String args) {
        String[] splitArgs = args.split("\\.");

        if (splitArgs.length != 2)
            return false;

        PRDProperty ticksProp = findPRDPropertyByName(world, splitArgs[0], splitArgs[1]);

        if (Objects.isNull(ticksProp))
            return false;

        return PropTypes.NUMERIC_PROPS.contains(property.getType());
    }
}
