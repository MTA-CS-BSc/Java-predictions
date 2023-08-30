package engine.parsers;

import engine.consts.SystemFunctions;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.RandomGenerator;
import engine.modules.Utils;
import engine.modules.ValidatorsUtils;
import engine.prototypes.implemented.actions.Action;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;

import java.util.Objects;

public abstract class ExpressionParser {
    public static String evaluateExpression(World world, Action action, String expression, SingleEntity on) throws PropertyNotFoundException {

        Property expressionEntityProp = Objects.isNull(on) ? Utils.findAnyPropertyByName(world, action.getEntityName(), expression)
                : Utils.findPropertyByName(on, expression);

        if (ValidatorsUtils.isSystemFunction(expression))
            return evaluateSystemExpression(world, action, expression, on);

        else if (!Objects.isNull(expressionEntityProp))
            return expressionEntityProp.getValue().getCurrentValue();

        return expression;
    }
    private static String evaluateSystemExpression(World world, Action action, String expression, SingleEntity on) throws PropertyNotFoundException {
        String systemFunctionType = Utils.getSystemFunctionType(expression);
        String systemFunctionValue = expression.substring(expression.lastIndexOf("(") + 1,
                expression.lastIndexOf(")"));

        switch (systemFunctionType) {
            case SystemFunctions.RANDOM:
                return evaluateSystemFuncRandom(systemFunctionValue);
            case SystemFunctions.ENVIRONMENT:
                return evaluateSystemFuncEnv(world, action, systemFunctionValue);
            case SystemFunctions.EVALUATE:
                return evaluateSystemFuncEvaluate(world, action, systemFunctionValue, on);
            case SystemFunctions.PERCENT:
                return evaluateSystemFuncPercent(world, action, systemFunctionValue, on);
            case SystemFunctions.TICKS:
                return evaluateSystemFuncTicks(world, action, systemFunctionValue, on);
        }

        return "";
    }
    private static String evaluateSystemFuncRandom(String systemFunctionValue) {
        return String.valueOf(RandomGenerator.randomizeRandomNumber(0, Integer.parseInt(systemFunctionValue)));
    }
    private static String evaluateSystemFuncEnv(World world, Action action, String systemFunctionValue) throws PropertyNotFoundException {
        Property envProp = Utils.findEnvironmentPropertyByName(world, systemFunctionValue);

        if (Objects.isNull(envProp))
            throw new PropertyNotFoundException(String.format("Action [%s]: environment(%s) does not exist",
                    action.getType(), systemFunctionValue));

        return envProp.getValue().getCurrentValue();
    }
    private static String evaluateSystemFuncEvaluate(World world, Action action,
                                                     String systemFunctionValue,
                                                     SingleEntity on) throws PropertyNotFoundException {
        String entityName = systemFunctionValue.split("\\.")[0];
        String propertyName = systemFunctionValue.split("\\.")[1];

        if (!Objects.isNull(on)) {
            Property property = Utils.findPropertyByName(on, propertyName);

            if (Objects.isNull(property))
                throw new PropertyNotFoundException(String.format("Action [%s]: Entity [%s]: Property [%s] not found",
                        action.getType(), entityName, propertyName));

            return property.getValue().getCurrentValue();
        }

        Property anyProp = Utils.findAnyPropertyByName(world, entityName, propertyName);

        if (Objects.isNull(anyProp))
            throw new PropertyNotFoundException(String.format("Action [%s]: Entity [%s]: System Function Evaluate: Property [%s] not found",
                    action.getType(), entityName, propertyName));

        return anyProp.getValue().getCurrentValue();
    }
    private static String evaluateSystemFuncPercent(World world, Action action,
                                                    String systemFunctionValue,
                                                    SingleEntity on) throws PropertyNotFoundException {
        String[] splitArgs = systemFunctionValue.split(",");

        return Utils.removeExtraZeroes(String.valueOf(Float.parseFloat(evaluateExpression(world, action, splitArgs[0], on)) /
                Float.parseFloat(evaluateExpression(world, action, splitArgs[1], on))));
    }
    private static String evaluateSystemFuncTicks(World world, Action action,
                                                  String systemFunctionValue,
                                                  SingleEntity on) throws PropertyNotFoundException {
        String[] splitArgs = systemFunctionValue.split("\\.");
        Property ticksProp = !Objects.isNull(on) ? Utils.findPropertyByName(on, splitArgs[1]) :
                Utils.findAnyPropertyByName(world, splitArgs[0], splitArgs[1]);

        if (Objects.isNull(ticksProp))
            throw new PropertyNotFoundException(String.format("Action [%s]: Ticks system function: can't find property [%s] of entity [%s]",
                    action.getType(), splitArgs[1], splitArgs[0]));

        return String.valueOf(ticksProp.getStableTime());
    }
}
