package engine.modules;

import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.World;
import engine.validators.Utils;
import engine.validators.actions.PRDActionValidators;

import java.util.Objects;

public class ExpressionParser {
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
    public static Object parseExpression(World world, String entityName, String expression) {
        Entity entity = (Entity)Utils.findEntityByName(world, entityName);

        if (Objects.isNull(entity))
            return null;

        if (PRDActionValidators.isSystemFunction(expression.substring(0, expression.indexOf("("))))
            return parseSystemFunctionExpression(world, expression);

        else if (entity.getProperties().getPropsMap().containsKey(expression))
            return entity.getProperties().getPropsMap().get(expression);

        return expression;
    }
}
