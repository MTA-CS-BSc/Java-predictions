package engine.modules;

import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.World;
import engine.validators.Utils;

import java.util.Arrays;
import java.util.Objects;

public class ExpressionParser {
    public static Class<?> sameType(String obj1, String obj2) {
        try {
            Integer.parseInt(obj1);
            Integer.parseInt(obj2);
            return Integer.class;
        }

        catch (Exception e) {
            try {
                Float.parseFloat(obj1);
                Float.parseFloat(obj2);
                return Float.class;
            }

            catch (Exception er) {
                return String.class;
            }
        }
    }
    public static Object parseSystemFunctionExpression(World world, String expression) {
        String functionName = expression.substring(0, expression.indexOf("("));
        String value = expression.substring(expression.indexOf("("), expression.lastIndexOf(")"));

        if (functionName.equalsIgnoreCase(SystemFunctions.RANDOM)) {
            try {
                return RandomGenerator.randomizeRandomNumber(Integer.parseInt(value));
            } catch (Exception e) {
                return null;
            }
        }

        else if (functionName.equalsIgnoreCase(SystemFunctions.ENVIRONMENT))
            return world.getEnvironment().getEnvVars().get(value);

        return null;
    }
    public static boolean isSystemFunction(String type) {
        return Arrays.asList(SystemFunctions.ENVIRONMENT, SystemFunctions.RANDOM).contains(type);
    }
    public static Object parseExpression(World world, String entityName, String expression) {
        Entity entity = (Entity)Utils.findEntityByName(world, entityName);

        if (Objects.isNull(entity))
            return null;

        if (expression.contains("(")
                && isSystemFunction(expression.substring(0, expression.indexOf("("))))
            return parseSystemFunctionExpression(world, expression);

        else if (entity.getProperties().getPropsMap().containsKey(expression))
            return entity.getProperties().getPropsMap().get(expression);

        return expression;
    }
}
