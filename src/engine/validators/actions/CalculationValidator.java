package engine.validators.actions;

import engine.consts.PropTypes;
import engine.exceptions.InvalidTypeException;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.ValidatorsUtils;
import engine.prototypes.jaxb.*;

import java.util.Objects;

public class CalculationValidator {
    public static boolean validate(PRDWorld world, PRDAction action) throws InvalidTypeException, PropertyNotFoundException {
        PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), action.getResultProp());
        PRDMultiply multiply = action.getPRDMultiply();
        PRDDivide divide = action.getPRDDivide();

        if (Objects.isNull(property))
            throw new PropertyNotFoundException(String.format("Action [%s]: Entity [%s]: Property [%s] does not exist",
                    action.getType(), action.getEntity(), action.getProperty()));

        else if (!PropTypes.NUMERIC_PROPS.contains(property.getType()))
            throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: Arithmetic operation on non-numeric type",
                    action.getType(), action.getEntity(), action.getBy()));

        if (!Objects.isNull(multiply))
            return ValidatorsUtils.validateExpressionType(world, action, property, multiply.getArg1())
                && ValidatorsUtils.validateExpressionType(world, action, property, multiply.getArg2());

        //TODO: Add validation arg2 != 0
        return ValidatorsUtils.validateExpressionType(world, action, property, divide.getArg1())
                && ValidatorsUtils.validateExpressionType(world, action, property, divide.getArg2());
    }
}
