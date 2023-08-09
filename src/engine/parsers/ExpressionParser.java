package engine.parsers;

import engine.consts.SystemFunctions;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.RandomGenerator;
import engine.modules.Utils;
import engine.modules.ValidatorsUtils;
import engine.prototypes.implemented.Action;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;

import java.util.Objects;

public class ExpressionParser {
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
        String systemFunctionType = ValidatorsUtils.getSystemFunctionType(expression);
        String systemFunctionValue = expression.substring(expression.lastIndexOf("(") + 1,
                expression.lastIndexOf(")"));

        switch (systemFunctionType) {
            case SystemFunctions.RANDOM:
                return evaluateSystemFuncRandom(systemFunctionValue);
            case SystemFunctions.ENVIRONMENT:
                return evaluateSystemFuncEnv(world, systemFunctionValue);
            case SystemFunctions.EVALUATE:
                return evaluateSystemFuncEvaluate(world, action, systemFunctionValue, on);
            case SystemFunctions.PERCENT:
                return evaluateSystemFuncPercent(world, action, systemFunctionValue, on);
        }

        return "";
    }
    private static String evaluateSystemFuncRandom(String bound) {
        return String.valueOf(RandomGenerator.randomizeRandomNumber(0, Integer.parseInt(bound)));
    }
    private static String evaluateSystemFuncEnv(World world, String propertyName) throws PropertyNotFoundException {
        Property envProp = world.getEnvironment().getEnvVars().values()
                .stream()
                .filter(element -> element.getName().equals(propertyName))
                .findFirst().orElse(null);

        if (Objects.isNull(envProp))
            throw new PropertyNotFoundException("environment(" + propertyName + ") does not exist");

        return envProp.getValue().getCurrentValue();
    }
    private static String evaluateSystemFuncEvaluate(World world, Action action, String propName, SingleEntity on) {
        if (!Objects.isNull(on))
            return Utils.findPropertyByName(on, propName).getValue().getCurrentValue();

        Property anyProp = Utils.findAnyPropertyByName(world, action.getEntityName(), propName);

        return !Objects.isNull(anyProp) && !anyProp.getValue().isRandomInitialize() ?
                anyProp.getValue().getCurrentValue() : "";
    }
    private static String evaluateSystemFuncPercent(World world, Action action, String args, SingleEntity on) throws PropertyNotFoundException {
        String[] splitArgs = args.split(",");
        return String.valueOf(Float.parseFloat(evaluateExpression(world, action, splitArgs[0], on)) /
                Float.parseFloat(evaluateExpression(world, action, splitArgs[1], on)));
    }
}
