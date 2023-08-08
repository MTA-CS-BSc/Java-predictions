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
import engine.prototypes.jaxb.PRDEnvProperty;

import java.util.Objects;

public class ExpressionParser {
    public static String evaluateExpression(World world, Action action, String expression, SingleEntity on) throws PropertyNotFoundException {
        Property expressionEntityProp = Objects.isNull(on) ? Utils.findAnyPropertyByName(world, action.getEntityName(), expression)
                : Utils.findPropertyByName(on, expression);

        if (ValidatorsUtils.isSystemFunction(expression)) {
            String systemFunctionType = ValidatorsUtils.getSystemFunctionType(expression);

            if (systemFunctionType.equals(SystemFunctions.RANDOM))
                return evaluateSystemFuncRandom(expression);

            else if (systemFunctionType.equals(SystemFunctions.ENVIRONMENT))
                return evaluateSystemFuncEnv(world, expression.substring(expression.lastIndexOf("(") + 1,
                        expression.lastIndexOf(")")));
        }

        else if (!Objects.isNull(expressionEntityProp))
            return expressionEntityProp.getValue().getCurrentValue();

        return expression;
    }
    public static String evaluateSystemFuncRandom(String expression) {
        String bound = expression.substring(expression.lastIndexOf("(") + 1,
                expression.lastIndexOf(")"));

        return String.valueOf(RandomGenerator.randomizeRandomNumber(0, Integer.parseInt(bound)));
    }
    public static String evaluateSystemFuncEnv(World world, String propertyName) throws PropertyNotFoundException {
        Property envProp = world.getEnvironment().getEnvVars().values()
                .stream()
                .filter(element -> element.getName().equals(propertyName))
                .findFirst().orElse(null);

        if (Objects.isNull(envProp))
            throw new PropertyNotFoundException("environment(" + propertyName + ") does not exist");

        return envProp.getValue().getCurrentValue();
    }
}
