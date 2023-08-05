package engine.parsers;

import engine.logs.Loggers;
import engine.consts.PropTypes;
import engine.modules.RandomGenerator;
import engine.consts.SystemFunctions;
import engine.modules.Utils;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.*;
import engine.validators.actions.PRDActionValidators;

import java.util.Objects;

public class ExpressionParser {
    private static Object parseRandomSystemFunctionExpression(String value) {
        try {
            return RandomGenerator.randomizeRandomNumber(0, Integer.parseInt(value));
        } catch (Exception e) {
            return null;
        }
    }
    private static Object parseEnvSystemFunctionExpression(Object world, String value) {
        if (world.getClass() == World.class)
            return ((World)world).getEnvironment().getEnvVars().get(value);

        else if (world.getClass() == PRDWorld.class)
            return ((PRDWorld)world).getPRDEvironment().getPRDEnvProperty()
                    .stream().filter(element -> element.getPRDName().equals(value))
                    .findFirst().orElse(null);

        return null;
    }
    private static Object parseEvaluateSystemFunctionExpression(Object world,
                                                              String value) {
        String entityName = value.split("\\.")[0];
        String propertyName = value.split("\\.")[1];
        Object property = Utils.findPropertyByName(world, entityName, propertyName);

        if (Objects.isNull(Utils.findEntityByName(world, entityName))
                || Objects.isNull(property))
            return null;

        return property;
    }
    private static float getPercentResult(String arg1, String arg2) {
        return Float.parseFloat(arg1) / Float.parseFloat(arg2);
    }
    private static float parsePercentSystemFunctionExpression(Object world,
                                                              PRDAction action,
                                                              String value) {
        String[] args = value.split(",");

        String arg1 = evaluateExpression(world, action, args[0]);
        String arg2 = evaluateExpression(world, action, args[1]);

        if (arg1.isEmpty() || arg2.isEmpty()
            || PRDActionValidators.validateExpressionIsNumeric(arg1)
                || PRDActionValidators.validateExpressionIsNumeric(arg2)) {
            Loggers.XML_ERRORS_LOGGER.info("Precent arguments are invalid");
            return 0;
        }

        return getPercentResult(arg1, arg2);
    }
    private static int parseTicksSystemFunctionExpression(Object world, String value) {
        Object property = parseEvaluateSystemFunctionExpression(world, value);

        if (Objects.isNull(property) || property.getClass() == PRDProperty.class) {
            Loggers.SIMULATION_LOGGER.info("Entity or property invalid in ticks system function");
            return 0;
        }

        if (property.getClass() == Property.class)
            return ((Property)property).getStableTime();

        return 0;
    }
    private static Object parseSystemFunctionExpression(Object world, PRDAction action,
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

        else if (functionName.equalsIgnoreCase(SystemFunctions.TICKS))
            return parseTicksSystemFunctionExpression(world, value);

        return null;
    }
    private static boolean isSystemFunction(String type) {
        return SystemFunctions.ALL_SYSTEM_FUNCS.contains(type);
    }

    private static Object parseExpression(Object world, PRDAction action, String expression) {
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

    public static String evaluateExpression(Object world, PRDAction action, String expression) {
        Object parsedExpression = parseExpression(world, action, expression);

        if (Objects.isNull(parsedExpression))
            return "";

        if (parsedExpression.getClass() == Property.class) {
            String propValue = ((Property) parsedExpression).getValue().getCurrentValue();

            if (propValue.isEmpty()) {
                if (PropTypes.NUMERIC_PROPS.contains(((Property)parsedExpression).getType()))
                    return "0";

                else if (PropTypes.BOOLEAN_PROPS.contains(((Property)parsedExpression).getType()))
                    return "false";
            }

            return propValue;
        }

        else if (parsedExpression.getClass() == PRDProperty.class) {
            if (((PRDProperty) parsedExpression).getPRDValue().isRandomInitialize()) {
                if (PropTypes.NUMERIC_PROPS.contains(((PRDProperty)parsedExpression).getType()))
                    return "0";

                else if (PropTypes.BOOLEAN_PROPS.contains(((PRDProperty)parsedExpression).getType()))
                    return "false";
            }

            return ((PRDProperty) parsedExpression).getPRDValue().getInit();
        }

        else if (parsedExpression.getClass() == PRDEnvProperty.class) {
                if (PropTypes.NUMERIC_PROPS.contains(((PRDEnvProperty)parsedExpression).getType()))
                    return "0";

                else if (PropTypes.BOOLEAN_PROPS.contains(((PRDEnvProperty)parsedExpression).getType()))
                    return "false";
        }

        else if (Utils.isDecimal(parsedExpression.toString()))
            return parsedExpression.toString();

        return "";
    }
}
