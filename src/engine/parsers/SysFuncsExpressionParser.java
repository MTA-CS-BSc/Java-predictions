package engine.parsers;

import engine.consts.BoolPropValues;
import engine.consts.PropTypes;
import engine.consts.SystemFunctions;
import engine.modules.RandomGenerator;
import engine.prototypes.implemented.Action;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDEnvProperty;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public class SysFuncsExpressionParser {
    public static String parseRandomExpression(String value) {
        if (value.isEmpty())
            return "";
        try {
            return String.valueOf(RandomGenerator.randomizeRandomNumber(0, Integer.parseInt(value)));
        } catch (Exception e) {
            return "";
        }
    }
    public static String parseEnvExpression(Object world, String value) {
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
    public static String parseExpression(World world, Action action,
                                                        String expression) {
        String functionName = expression.substring(0, expression.indexOf("("));
        String value = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

        if (functionName.equalsIgnoreCase(SystemFunctions.RANDOM))
            return parseRandomExpression(value);

        if (functionName.equalsIgnoreCase(SystemFunctions.ENVIRONMENT))
            return parseEnvExpression(world, value);

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
}
