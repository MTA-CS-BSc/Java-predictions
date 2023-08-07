package engine.parsers;

import engine.consts.BoolPropValues;
import engine.consts.PropTypes;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import engine.prototypes.jaxb.*;

import java.util.Objects;

public class ExpressionParser {
    public static String parseExpression(World world, Action action, String expression) {
        if (expression.contains("(")
            && SysFuncsExpressionParser.isSystemFunction(expression.substring(0, expression.indexOf("("))))
                return SysFuncsExpressionParser.parseExpression(world, action, expression);

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
