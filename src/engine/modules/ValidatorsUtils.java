package engine.modules;

import helpers.types.PropTypes;
import engine.consts.SystemFunctions;
import engine.prototypes.jaxb.*;
import helpers.Constants;
import helpers.types.TypesUtils;

import java.util.Objects;
import java.util.regex.Pattern;

public abstract class ValidatorsUtils {
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
        String pattern = "^[A-Za-z]{1,}\\(.+\\)$";

        if (!Pattern.matches(pattern, expression))
            return false;

        return SystemFunctions.ALL_SYSTEM_FUNCS.contains(expression.substring(0, expression.indexOf("(")));
    }
    public static String getExpressionType(PRDWorld world, PRDAction action, String expression) {
        PRDProperty expressionEntityProp = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), expression);

        if (ValidatorsUtils.isSystemFunction(expression)) {
            String systemFunctionValue = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

            switch (TypesUtils.getSystemFunctionType(expression)) {
                case SystemFunctions.RANDOM:
                case SystemFunctions.TICKS:
                case SystemFunctions.PERCENT:
                    return PropTypes.DECIMAL;
                case SystemFunctions.ENVIRONMENT:
                    PRDEnvProperty envProp = world.getPRDEnvironment().getPRDEnvProperty()
                            .stream()
                            .filter(element -> element.getPRDName().equals(systemFunctionValue))
                            .findFirst().orElse(null);

                    assert !Objects.isNull(envProp);
                    return envProp.getType();
                case SystemFunctions.EVALUATE:
                    String entityName = systemFunctionValue.split("\\.")[0];
                    String propName = systemFunctionValue.split("\\.")[1];

                    PRDProperty property = findPRDPropertyByName(world, entityName, propName);

                    assert !Objects.isNull(property);
                    return property.getType();
            }
        }

        else if (!Objects.isNull(expressionEntityProp))
            return expressionEntityProp.getType();

        else if (TypesUtils.isDecimal(expression))
            return PropTypes.DECIMAL;

        else if (TypesUtils.isFloat(expression))
            return PropTypes.FLOAT;

        else if (TypesUtils.isBoolean(expression))
            return PropTypes.BOOLEAN;

        return PropTypes.STRING;
    }
    public static boolean validateExpressionType(PRDWorld world, PRDAction action, String expectedType, String expression) {
        PRDProperty expressionEntityProp = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), expression);

        if (ValidatorsUtils.isSystemFunction(expression))
            return validateSystemFuncExpressionType(world, action, expectedType, expression);

        else if (!Objects.isNull(expressionEntityProp))
            return expressionEntityProp.getType().equals(expectedType)
                    || (expressionEntityProp.getType().equals(PropTypes.DECIMAL)
                    && expectedType.equals(PropTypes.FLOAT));

        else {
            if (TypesUtils.isDecimal(expression))
                return PropTypes.NUMERIC_PROPS.contains(expectedType);

            else if (TypesUtils.isFloat(expression)) {
                if (!expression.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT))
                    return expectedType.equals(PropTypes.FLOAT);

                return PropTypes.NUMERIC_PROPS.contains(expectedType);
            }

            else if (TypesUtils.isBoolean(expression))
                return expectedType.equals(PropTypes.BOOLEAN);
        }

        return expectedType.equals(PropTypes.STRING);
    }
    private static boolean validateSystemFuncRandom(String expectedType, String systemFunctionValue) {
        return PropTypes.NUMERIC_PROPS.contains(expectedType) && TypesUtils.isDecimal(systemFunctionValue);
    }
    private static boolean validateSystemFuncExpressionType(PRDWorld world, PRDAction action,
                                                            String expectedType, String expression) {
        String systemFunctionType = TypesUtils.getSystemFunctionType(expression);
        String systemFunctionValue = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

        switch (systemFunctionType) {
            case SystemFunctions.RANDOM:
                return validateSystemFuncRandom(expectedType, systemFunctionValue);
            case SystemFunctions.ENVIRONMENT:
                return validateSystemFuncEnv(world, expectedType, systemFunctionValue);
            case SystemFunctions.EVALUATE:
                return validateSystemFuncEvaluate(world, systemFunctionValue.split("\\.")[0], expectedType, systemFunctionValue.split("\\.")[1]);
            case SystemFunctions.PERCENT:
                return validateSystemFuncPercent(world, action, expectedType, systemFunctionValue);
            case SystemFunctions.TICKS:
                return validateSystemFuncTicks(world, expectedType, systemFunctionValue);
        }

        return false;
    }
    private static boolean validateSystemFuncEnv(PRDWorld world, String expectedType, String systemFunctionValue) {
        PRDEnvProperty envProp = world.getPRDEnvironment().getPRDEnvProperty()
                .stream()
                .filter(element -> element.getPRDName().equals(systemFunctionValue))
                .findFirst().orElse(null);

        return (!Objects.isNull(envProp) &&
                (envProp.getType().equals(expectedType)
                || (envProp.getType().equals(PropTypes.DECIMAL) && expectedType.equals(PropTypes.FLOAT))));
    }
    private static boolean validateSystemFuncEvaluate(PRDWorld world, String entityName, String expectedType,
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

        return systemFuncProp.getType().equals(expectedType)
                || (systemFuncProp.getType().equals(PropTypes.DECIMAL) && expectedType.equals(PropTypes.FLOAT));
    }
    private static boolean validateSystemFuncPercent(PRDWorld world, PRDAction action, String expectedType, String args) {
        String[] splitArgs = args.split(",");

        if (splitArgs.length != 2)
            return false;

        String firstArgExpressionType = getExpressionType(world, action, splitArgs[0]);
        String secondArgExpressionType = getExpressionType(world, action, splitArgs[1]);

        return PropTypes.NUMERIC_PROPS.contains(firstArgExpressionType)
                && PropTypes.NUMERIC_PROPS.contains(secondArgExpressionType);
    }
    private static boolean validateSystemFuncTicks(PRDWorld world, String expectedType, String args) {
        String[] splitArgs = args.split("\\.");

        if (splitArgs.length != 2)
            return false;

        PRDProperty ticksProp = findPRDPropertyByName(world, splitArgs[0], splitArgs[1]);

        if (Objects.isNull(ticksProp))
            return false;

        return PropTypes.NUMERIC_PROPS.contains(expectedType);
    }
}
