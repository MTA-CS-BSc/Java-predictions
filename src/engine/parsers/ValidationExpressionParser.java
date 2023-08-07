package engine.parsers;

import engine.consts.BoolPropValues;
import engine.consts.PropTypes;
import engine.consts.SystemFunctions;
import engine.modules.Utils;
import engine.modules.ValidatorsUtils;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public class ValidationExpressionParser {
    private static String parseSysFuncExpression(PRDWorld world, PRDAction action,
                                                       String expression) {
        String functionName = expression.substring(0, expression.indexOf("("));
        String value = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

        if (functionName.equalsIgnoreCase(SystemFunctions.RANDOM))
            return SysFuncsExpressionParser.parseRandomExpression(value);

        if (functionName.equalsIgnoreCase(SystemFunctions.ENVIRONMENT))
            return SysFuncsExpressionParser.parseEnvExpression(world, value);

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
    public static String parseExpression(PRDWorld world, PRDAction action, String expression) {
        if (expression.contains("(")
                && SysFuncsExpressionParser.isSystemFunction(expression.substring(0, expression.indexOf("("))))
            return parseSysFuncExpression(world, action, expression);

        if (!Objects.isNull(ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), expression)))
            return "property-" + expression;

        return expression;
    }
    public static String getExpressionType(PRDWorld world, PRDAction action, String parsedExpression) {
        if (parsedExpression.contains("property")) {
            PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), parsedExpression.split("-")[1]);

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
