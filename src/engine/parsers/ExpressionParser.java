package engine.parsers;

import engine.consts.BoolPropValues;
import engine.logs.Loggers;
import engine.consts.PropTypes;
import engine.modules.RandomGenerator;
import engine.consts.SystemFunctions;
import engine.modules.Utils;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.*;
import engine.validators.actions.PRDActionValidators;

import java.util.Objects;

public class ExpressionParser {
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
    private static String parseSystemFunctionExpression(Object world, PRDAction action,
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
    private static boolean isSystemFunction(String type) {
        return SystemFunctions.ALL_SYSTEM_FUNCS.contains(type);
    }
    public static String parseExpression(World world, PRDAction action, String expression) {
        if (expression.contains("(")
            && isSystemFunction(expression.substring(0, expression.indexOf("("))))
                return parseSystemFunctionExpression(world, action, expression);

        SingleEntity someEntity = world.getEntities()
                .getEntitiesMap()
                .get(action.getEntity())
                .getSingleEntities().get(0);

        if (someEntity.getProperties().getPropsMap().containsKey(expression))
            return "property-" + expression;

        return expression;
    }

    public static String parseExpression(PRDWorld world, PRDAction action, String expression) {
        if (expression.contains("(")
                && isSystemFunction(expression.substring(0, expression.indexOf("("))))
            return parseSystemFunctionExpression(world, action, expression);

        if (!Objects.isNull(Utils.findPRDPropertyByName(world, action.getEntity(), expression)))
            return "property-" + expression;

        return expression;
    }

    public static String getExpressionType(PRDWorld world, PRDAction action, String parsedExpression) {
        if (parsedExpression.contains("property")) {
            PRDProperty property = Utils.findPRDPropertyByName(world, action.getEntity(), parsedExpression.split("-")[1]);

            if (Objects.isNull(property))
                return "";

            return property.getType();
        }

        if (Utils.isDecimal(parsedExpression))
            return PropTypes.DECIMAL;

        else if (parsedExpression.equals(BoolPropValues.TRUE) || parsedExpression.equals(BoolPropValues.FALSE))
            return PropTypes.BOOLEAN;

        return PropTypes.STRING;
    }
}
