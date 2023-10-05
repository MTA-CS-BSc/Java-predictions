package parsers;

import consts.SystemFunctions;
import exceptions.PropertyNotFoundException;
import modules.RandomGenerator;
import modules.Utils;
import modules.ValidatorsUtils;
import prototypes.prd.implemented.Property;
import prototypes.SingleEntity;
import prototypes.prd.implemented.World;
import types.TypesUtils;

import java.util.Objects;

public abstract class ExpressionParser {
    public static String evaluateExpression(World world, String expression,
                                            SingleEntity main, SingleEntity secondary) throws PropertyNotFoundException {
        Property expressionEntityProp = Utils.findPropertyByName(main, expression);

        if (ValidatorsUtils.isSystemFunction(expression))
            return evaluateSystemExpression(world, expression, main, secondary);

        else if (!Objects.isNull(expressionEntityProp))
            return expressionEntityProp.getValue().getCurrentValue();

        return expression;
    }

    private static String evaluateSystemExpression(World world, String expression,
                                                   SingleEntity main, SingleEntity secondary) throws PropertyNotFoundException {
        String systemFunctionType = TypesUtils.getSystemFunctionType(expression);
        String systemFunctionValue = expression.substring(expression.indexOf("(") + 1,
                expression.lastIndexOf(")"));

        switch (systemFunctionType) {
            case SystemFunctions.RANDOM:
                return evaluateSystemFuncRandom(systemFunctionValue);
            case SystemFunctions.ENVIRONMENT:
                return evaluateSystemFuncEnv(world, systemFunctionValue);
            case SystemFunctions.EVALUATE:
                return evaluateSystemFuncEvaluate(systemFunctionValue, main, secondary);
            case SystemFunctions.PERCENT:
                return evaluateSystemFuncPercent(world, systemFunctionValue, main, secondary);
            case SystemFunctions.TICKS:
                return evaluateSystemFuncTicks(systemFunctionValue, main, secondary);
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

    private static String evaluateSystemFuncEvaluate(String systemFunctionValue,
                                                     SingleEntity main,
                                                     SingleEntity secondary) throws PropertyNotFoundException {
        String entityName = systemFunctionValue.split("\\.")[0];
        String propertyName = systemFunctionValue.split("\\.")[1];
        SingleEntity on = entityName.equals(main.getEntityName()) ? main : secondary;
        Property property = Utils.findPropertyByName(on, propertyName);

        if (Objects.isNull(property))
            throw new PropertyNotFoundException(String.format("Entity [%s]: Property [%s] not found",
                    entityName, propertyName));

        return property.getValue().getCurrentValue();
    }

    private static String evaluateSystemFuncPercent(World world,
                                                    String systemFunctionValue,
                                                    SingleEntity main,
                                                    SingleEntity secondary) throws PropertyNotFoundException {
        String[] args = systemFunctionValue.split(",");
        float arg1 = Float.parseFloat(evaluateExpression(world, args[0], main, secondary));
        float arg2 = Float.parseFloat(evaluateExpression(world, args[1], main, secondary));

        return TypesUtils.removeExtraZeroes(String.valueOf(arg1 / arg2));
    }

    private static String evaluateSystemFuncTicks(String systemFunctionValue,
                                                  SingleEntity main,
                                                  SingleEntity secondary) throws PropertyNotFoundException {
        String entityName = systemFunctionValue.split("\\.")[0];
        String propertyName = systemFunctionValue.split("\\.")[1];
        SingleEntity on = entityName.equals(main.getEntityName()) ? main : secondary;
        Property ticksProp = Utils.findPropertyByName(on, propertyName);

        if (Objects.isNull(ticksProp))
            throw new PropertyNotFoundException(String.format("Ticks system function: can't find property [%s] of entity [%s]",
                    propertyName, entityName));

        return String.valueOf(ticksProp.getStableTime());
    }
}
