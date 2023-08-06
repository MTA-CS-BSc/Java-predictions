package engine.parsers;

import engine.consts.BoolPropValues;
import engine.logs.Loggers;
import engine.consts.PropTypes;
import engine.modules.RandomGenerator;
import engine.consts.SystemFunctions;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import engine.prototypes.jaxb.*;

import java.util.Objects;

public class ExpressionParser {
    public static String parseSystemFunctionExpression(PRDWorld world, PRDAction action,
                                                        String expression) {
        String functionName = expression.substring(0, expression.indexOf("("));
        String value = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

        if (functionName.equalsIgnoreCase(SystemFunctions.RANDOM))
            return parseRandomSystemFunctionExpression(value);

        if (functionName.equalsIgnoreCase(SystemFunctions.ENVIRONMENT))
            return parseEnvSystemFunctionExpression(world, value);

//        else if (functionName.equalsIgnoreCase(SystemFunctions.EVALUATE))
//            return parseEvaluateSystemFunctionExpression(world, value);
//
//        else if (functionName.equalsIgnoreCase(SystemFunctions.PRECENT))
//            return parsePercentSystemFunctionExpression(world, action, value);
//
//        else if (functionName.equalsIgnoreCase(SystemFunctions.TICKS))
//            return parseTicksSystemFunctionExpression(world, value);

        return null;
    }
    private static String parseRandomSystemFunctionExpression(String value) {
        if (value.isEmpty())
            return "";
        try {
            return String.valueOf(RandomGenerator.randomizeRandomNumber(0, Integer.parseInt(value)));
        } catch (Exception e) {
            return "";
        }
    }
    private static String parseEnvSystemFunctionExpression(Object world, String value) {
        if (world.getClass() == World.class)
            return ((World) world).getEnvironment().getEnvVars().get(value).getValue().getCurrentValue();

        PRDEnvProperty prop = ((PRDWorld) world).getPRDEvironment().getPRDEnvProperty()
                .stream()
                .filter(element -> element.getPRDName().equals(value))
                .findFirst().orElse(null);

        if (Objects.isNull(prop))
            return "";

        if (PropTypes.BOOLEAN_PROPS.contains(prop.getType()))
            return BoolPropValues.TRUE;

        else if (PropTypes.NUMERIC_PROPS.contains(prop.getType()))
            return "0";

        return "";
    }
    private static String parseSystemFunctionExpression(World world, Action action,
                                                        String expression) {
        String functionName = expression.substring(0, expression.indexOf("("));
        String value = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

        if (functionName.equalsIgnoreCase(SystemFunctions.RANDOM))
            return parseRandomSystemFunctionExpression(value);

        if (functionName.equalsIgnoreCase(SystemFunctions.ENVIRONMENT))
            return parseEnvSystemFunctionExpression(world, value);

//        else if (functionName.equalsIgnoreCase(SystemFunctions.EVALUATE))
//            return parseEvaluateSystemFunctionExpression(world, value);
//
//        else if (functionName.equalsIgnoreCase(SystemFunctions.PRECENT))
//            return parsePercentSystemFunctionExpression(world, action, value);
//
//        else if (functionName.equalsIgnoreCase(SystemFunctions.TICKS))
//            return parseTicksSystemFunctionExpression(world, value);

        return null;
    }
    public static boolean isSystemFunction(String type) {
        return SystemFunctions.ALL_SYSTEM_FUNCS.contains(type);
    }
    public static String parseExpression(World world, Action action, String expression) {
        if (expression.contains("(")
            && isSystemFunction(expression.substring(0, expression.indexOf("("))))
                return parseSystemFunctionExpression(world, action, expression);

        Property prop = Utils.findAnyPropertyByName(world, action.getEntityName(), expression);

        if (!Objects.isNull(prop))
            return "property-" + expression;

        return expression;
    }
    public static String getExpressionType(World world, PRDAction action, String parsedExpression) {
        if (parsedExpression.contains("property")) {
            Property prop =  Utils.findAnyPropertyByName(world, action.getEntity(), parsedExpression.split("-")[1]);

            if (Objects.isNull(prop))
                return "";

            return prop.getType();
        }

        if (Utils.isDecimal(parsedExpression))
            return PropTypes.DECIMAL;

        else if (parsedExpression.equals(BoolPropValues.TRUE) || parsedExpression.equals(BoolPropValues.FALSE))
            return PropTypes.BOOLEAN;

        return PropTypes.STRING;
    }
    public static String evaluateExpression(String parsedValue, SingleEntity entity) {
        return parsedValue.contains("property") ?
                Utils.getPropertyValueForEntity(entity, parsedValue.split("-")[1]) : parsedValue;
    }
}
