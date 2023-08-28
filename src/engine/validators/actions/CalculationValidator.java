package engine.validators.actions;

import engine.consts.PropTypes;
import engine.exceptions.InvalidTypeException;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.ValidatorsUtils;
import engine.prototypes.jaxb.*;

import java.util.Objects;

public abstract class CalculationValidator {
    public static boolean validate(PRDWorld world, PRDAction action) throws InvalidTypeException, PropertyNotFoundException {
        PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), action.getResultProp());
        PRDMultiply multiply = action.getPRDMultiply();
        PRDDivide divide = action.getPRDDivide();

        if (Objects.isNull(property))
            throw new PropertyNotFoundException(String.format("Action [%s]: Entity [%s]: Property [%s] does not exist",
                    action.getType(), action.getEntity(), action.getProperty()));

        else if (!PropTypes.NUMERIC_PROPS.contains(property.getType()))
            throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: Arithmetic operation on non-numeric type",
                    action.getType(), action.getEntity()));

        if (!Objects.isNull(multiply))
            if (!ValidatorsUtils.validateExpressionType(world, action, property.getType(), multiply.getArg1())
                || !ValidatorsUtils.validateExpressionType(world, action, property.getType(), multiply.getArg2()))
                throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: Arithmetic operation must receive arithmetic args",
                        action.getType(), action.getEntity()));

        else if (!Objects.isNull(divide)) {
                if (!ValidatorsUtils.validateExpressionType(world, action, property.getType(), divide.getArg1())
                        || !ValidatorsUtils.validateExpressionType(world, action, property.getType(), divide.getArg2()))
                    throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: Arithmetic operation must receive arithmetic args",
                            action.getType(), action.getEntity()));


                else if (divide.getArg2().equals("0"))
                    throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: Can't divide by zero",
                            action.getType(), action.getEntity()));
            }

        return true;
    }
}
