package engine.validators.actions;

import engine.consts.PropTypes;
import engine.exceptions.InvalidTypeException;
import engine.parsers.ValidationExpressionParser;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDDivide;
import engine.prototypes.jaxb.PRDMultiply;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public class CalculationValidator {
    private static boolean validateArgs(PRDWorld world, PRDAction action,
                                                   String arg1, String arg2) throws InvalidTypeException {
        String arg1Type = ValidationExpressionParser.getExpressionType(world, action, arg1);
        String arg2Type = ValidationExpressionParser.getExpressionType(world, action, arg2);

        if (!PropTypes.NUMERIC_PROPS.contains(arg1Type) || !PropTypes.NUMERIC_PROPS.contains(arg2Type))
            throw new InvalidTypeException(String.format("Action [%s], one or more of the provided properties" +
                    "has incorrect type", action.getType()));

        return true;
    }
    public static boolean validate(PRDWorld world, PRDAction action) throws InvalidTypeException {
        PRDMultiply multiply = action.getPRDMultiply();
        PRDDivide divide = action.getPRDDivide();
        String arg1 = ValidationExpressionParser.parseExpression(world, action,
                Objects.isNull(multiply) ? divide.getArg1() : multiply.getArg1());

        String arg2 = ValidationExpressionParser.parseExpression(world, action,
                Objects.isNull(multiply) ? divide.getArg2() : multiply.getArg2());

        return validateArgs(world, action, arg1, arg2);
    }
}
