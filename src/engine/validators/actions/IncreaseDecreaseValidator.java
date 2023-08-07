package engine.validators.actions;

import engine.consts.PropTypes;
import engine.exceptions.InvalidTypeException;
import engine.parsers.ValidationExpressionParser;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDWorld;

public class IncreaseDecreaseValidator {
    public static boolean validate(PRDWorld world, PRDAction action) throws InvalidTypeException {
        String val = ValidationExpressionParser.parseExpression(world, action, action.getBy());
        String parsedValType = ValidationExpressionParser.getExpressionType(world, action, val);

        if (!PropTypes.NUMERIC_PROPS.contains(parsedValType))
            throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: action's by value [%s] is not numeric",
                    action.getType(), action.getEntity(), action.getBy()));

        return true;
    }
}
