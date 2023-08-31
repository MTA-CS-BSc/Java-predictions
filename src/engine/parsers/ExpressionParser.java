package engine.parsers;

import engine.consts.SystemFunctions;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.RandomGenerator;
import engine.modules.Utils;
import engine.modules.ValidatorsUtils;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;

import java.util.Objects;

public abstract class ExpressionParser {
    public static String evaluateExpression(World world, String expression, SingleEntity main) throws PropertyNotFoundException {
        Property expressionEntityProp = Utils.findPropertyByName(main, expression);

        if (ValidatorsUtils.isSystemFunction(expression))
            return evaluateSystemExpression(world, expression, main);

        else if (!Objects.isNull(expressionEntityProp))
            return expressionEntityProp.getValue().getCurrentValue();

        return expression;
    }
    private static String evaluateSystemExpression(World world, String expression, SingleEntity main) throws PropertyNotFoundException {
        String systemFunctionType = Utils.getSystemFunctionType(expression);
        String systemFunctionValue = expression.substring(expression.lastIndexOf("(") + 1,
                expression.lastIndexOf(")"));

        switch (systemFunctionType) {
            case SystemFunctions.RANDOM:
                return evaluateSystemFuncRandom(systemFunctionValue);
            case SystemFunctions.ENVIRONMENT:
                return evaluateSystemFuncEnv(world, systemFunctionValue);
            case SystemFunctions.EVALUATE:
                return evaluateSystemFuncEvaluate(world, systemFunctionValue, main);
            case SystemFunctions.PERCENT:
                return evaluateSystemFuncPercent(world, systemFunctionValue, main);
            case SystemFunctions.TICKS:
                return evaluateSystemFuncTicks(world, systemFunctionValue, main);
        }

        return "";
    }
    private static String evaluateSystemFuncRandom(String systemFunctionValue) {
        return String.valueOf(RandomGenerator.randomizeRandomNumber(0, Integer.parseInt(systemFunctionValue)));
    }
    private static String evaluateSystemFuncEnv(World world, String systemFunctionValue) throws PropertyNotFoundException {
        Property envProp = Utils.findEnvironmentPropertyByName(world, systemFunctionValue);

        if (Objects.isNull(envProp))
            throw new PropertyNotFoundException(String.format("environment(%s) does not exist", systemFunctionValue));

        return envProp.getValue().getCurrentValue();
    }
    private static String evaluateSystemFuncEvaluate(World world,
                                                     String systemFunctionValue,
                                                     SingleEntity main) throws PropertyNotFoundException {
        String entityName = systemFunctionValue.split("\\.")[0];
        String propertyName = systemFunctionValue.split("\\.")[1];

        if (!Objects.isNull(main)) {
            Property property = Utils.findPropertyByName(main, propertyName);

            if (Objects.isNull(property))
                throw new PropertyNotFoundException(String.format("Entity [%s]: Property [%s] not found",
                        entityName, propertyName));

            return property.getValue().getCurrentValue();
        }

        Property anyProp = Utils.findAnyPropertyByName(world, entityName, propertyName);

        if (Objects.isNull(anyProp))
            throw new PropertyNotFoundException(String.format("Entity [%s]: System Function Evaluate: Property [%s] not found",
                    entityName, propertyName));

        return anyProp.getValue().getCurrentValue();
    }
    private static String evaluateSystemFuncPercent(World world,
                                                    String systemFunctionValue,
                                                    SingleEntity main) throws PropertyNotFoundException {
        String[] splitArgs = systemFunctionValue.split(",");

        return Utils.removeExtraZeroes(String.valueOf(Float.parseFloat(evaluateExpression(world, splitArgs[0], main)) /
                Float.parseFloat(evaluateExpression(world, splitArgs[1], main))));
    }
    private static String evaluateSystemFuncTicks(World world,
                                                  String systemFunctionValue,
                                                  SingleEntity main) throws PropertyNotFoundException {
        String[] splitArgs = systemFunctionValue.split("\\.");
        Property ticksProp = !Objects.isNull(main) ? Utils.findPropertyByName(main, splitArgs[1]) :
                Utils.findAnyPropertyByName(world, splitArgs[0], splitArgs[1]);

        if (Objects.isNull(ticksProp))
            throw new PropertyNotFoundException(String.format("Ticks system function: can't find property [%s] of entity [%s]",
                    splitArgs[1], splitArgs[0]));

        return String.valueOf(ticksProp.getStableTime());
    }
}
