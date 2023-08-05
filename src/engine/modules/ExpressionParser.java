package engine.modules;

import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDEntity;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.Utils;

import java.util.Arrays;
import java.util.Objects;

public class ExpressionParser {

    public static Object parseRandomSystemFunctionExpression(String value) {
        try {
            return RandomGenerator.randomizeRandomNumber(Integer.parseInt(value));
        } catch (Exception e) {
            return null;
        }
    }
    public static Object parseEnvSystemFunctionExpression(Object world, String functionName,
                                                          String value) {
        if (functionName.equalsIgnoreCase(SystemFunctions.ENVIRONMENT)
                && world.getClass() == World.class)
            return ((World)world).getEnvironment().getEnvVars().get(value);

        else if (functionName.equalsIgnoreCase(SystemFunctions.ENVIRONMENT)
                && world.getClass() == PRDWorld.class)
            return ((PRDWorld)world).getPRDEvironment().getPRDEnvProperty()
                    .stream().filter(element -> element.getPRDName().equals(value))
                    .findFirst().orElse(null);

        return null;
    }
    public static Object parseSystemFunctionExpression(Object world, String expression) {
        String functionName = expression.substring(0, expression.indexOf("("));
        String value = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

        if (functionName.equalsIgnoreCase(SystemFunctions.RANDOM))
            return parseRandomSystemFunctionExpression(value);

        return parseEnvSystemFunctionExpression(world, functionName, value);
    }
    public static boolean isSystemFunction(String type) {
        return Arrays.asList(SystemFunctions.ENVIRONMENT, SystemFunctions.RANDOM).contains(type);
    }

    /* Returns PRDEnvProperty/PRDProperty/Property/int (if random)/String (default) */
    public static Object parseExpression(Object world, String entityName, String expression) {
        Object entity = Utils.findEntityByName(world, entityName);

        if (Objects.isNull(entity))
            return null;

        if (expression.contains("(")
                && isSystemFunction(expression.substring(0, expression.indexOf("("))))
            return parseSystemFunctionExpression(world, expression);

        if (entity.getClass() == Entity.class) {
            if (((Entity)entity).getProperties().getPropsMap().containsKey(expression))
                return (((Entity)entity).getProperties().getPropsMap().get(expression));
        }

        else if (entity.getClass() == PRDEntity.class) {
            PRDProperty prop = (((PRDEntity)entity).getPRDProperties().getPRDProperty()
                    .stream()
                    .filter(element -> element.getPRDName().equals(expression))
                    .findAny().orElse(null));

            if (!Objects.isNull(prop))
                return prop;
        }

        return expression;
    }
}
