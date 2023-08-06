package engine.parsers;

import engine.consts.BoolPropValues;
import engine.consts.PropTypes;
import engine.modules.Utils;
import engine.modules.ValidatorsUtils;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public class ValidationExpressionParser {
    public static String parseExpression(PRDWorld world, PRDAction action, String expression) {
        if (expression.contains("(")
                && ExpressionParser.isSystemFunction(expression.substring(0, expression.indexOf("("))))
            return ExpressionParser.parseSystemFunctionExpression(world, action, expression);

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
