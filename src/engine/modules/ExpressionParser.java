package engine.modules;

import engine.logs.Loggers;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDEntity;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.actions.PRDActionValidators;

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
    public static Object parseEnvSystemFunctionExpression(Object world,
                                                          String value) {
        if (world.getClass() == World.class)
            return ((World)world).getEnvironment().getEnvVars().get(value);

        else if (world.getClass() == PRDWorld.class)
            return ((PRDWorld)world).getPRDEvironment().getPRDEnvProperty()
                    .stream().filter(element -> element.getPRDName().equals(value))
                    .findFirst().orElse(null);

        return null;
    }
    public static Object parseEvaluateSystemFunctionExpression(Object world,
                                                              String value) {
        String entityName = value.split("\\.")[0];
        String propertyName = value.split("\\.")[1];
        Object property = Utils.findPropertyByName(world, entityName, propertyName);

        if (Objects.isNull(Utils.findEntityByName(world, entityName))
                || Objects.isNull(property))
            return null;

        return property;
    }
    public static float getPercentResult(Object arg1, Object arg2) {
        float arg1_double = 0;
        float arg2_double = 1;

        if (arg1.getClass() == Property.class)
            arg1_double = Float.parseFloat(((Property)arg1).getValue().getInit());

        else if (arg1.getClass() == Integer.class || arg1.getClass() == Float.class)
            arg1_double = (float)arg1;

        if (arg2.getClass() == Property.class)
            arg2_double = Float.parseFloat(((Property)arg2).getValue().getInit());

        else if (arg2.getClass() == Integer.class || arg1.getClass() == Float.class)
            arg2_double = (float)arg2;

        return arg1_double / arg2_double;
    }
    public static Float parsePercentSystemFunctionExpression(Object world,
                                                              PRDAction action,
                                                              String value) {
        String[] args = value.split(",");

        Object arg1 = parseExpression(world, action, args[0]);
        Object arg2 = parseExpression(world, action, args[1]);

        if (Objects.isNull(arg1) || Objects.isNull(arg2)
            || PRDActionValidators.validateExpressionIsNumeric(arg1)
                || PRDActionValidators.validateExpressionIsNumeric(arg2)) {
            Loggers.XML_ERRORS_LOGGER.info("Precent arguments are invalid");
            return null;
        }

        return getPercentResult(arg1, arg2);
    }
    public static Object parseSystemFunctionExpression(Object world, PRDAction action,
                                                       String expression) {
        String functionName = expression.substring(0, expression.indexOf("("));
        String value = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

        if (functionName.equalsIgnoreCase(SystemFunctions.RANDOM))
            return parseRandomSystemFunctionExpression(value);

        if (functionName.equalsIgnoreCase(SystemFunctions.ENVIRONMENT))
            return parseEnvSystemFunctionExpression(world, value);

        else if (functionName.equalsIgnoreCase(SystemFunctions.EVALUATE))
            return parseEvaluateSystemFunctionExpression(world, value);

        else if (functionName.equalsIgnoreCase(SystemFunctions.PRECENT))
            return parsePercentSystemFunctionExpression(world, action, value);

        return null;
    }
    public static boolean isSystemFunction(String type) {
        return Arrays.asList(SystemFunctions.ENVIRONMENT, SystemFunctions.RANDOM).contains(type);
    }

    /* Returns PRDEnvProperty/PRDProperty/Property/int/float (if random)/String (default) */
    public static Object parseExpression(Object world, PRDAction action, String expression) {
        if (expression.contains("(")
                && isSystemFunction(expression.substring(0, expression.indexOf("("))))
            return parseSystemFunctionExpression(world, action, expression);

        String entityName = action.getEntity();
        Object entity = Utils.findEntityByName(world, entityName);

        if (Objects.isNull(entity))
            return null;

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
