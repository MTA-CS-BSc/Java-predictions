package validators.actions;

import exceptions.InvalidTypeException;
import exceptions.PropertyNotFoundException;
import modules.ValidatorsUtils;
import prototypes.jaxb.PRDAction;
import prototypes.jaxb.PRDProperty;
import prototypes.jaxb.PRDWorld;
import types.PropTypes;

import java.util.Objects;

public abstract class IncreaseDecreaseValidator {
    public static boolean validate(PRDWorld world, PRDAction action) throws InvalidTypeException, PropertyNotFoundException {
        PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), action.getProperty());

        if (Objects.isNull(property))
            throw new PropertyNotFoundException(String.format("Action [%s]: Entity [%s]: Property [%s] does not exist",
                    action.getType(), action.getEntity(), action.getProperty()));

        else if (!PropTypes.NUMERIC_PROPS.contains(property.getType()))
            throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: Arithmetic operation on non-numeric type",
                    action.getType(), action.getEntity()));

        if (!ValidatorsUtils.validateExpressionType(world, action, property.getType(), action.getBy()))
            throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: Arithmetic operation must receive arithmetic args",
                    action.getType(), action.getEntity()));

        return true;
    }
}
