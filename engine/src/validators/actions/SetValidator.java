package validators.actions;

import exceptions.InvalidTypeException;
import exceptions.PropertyNotFoundException;
import modules.ValidatorsUtils;
import prototypes.jaxb.PRDAction;
import prototypes.jaxb.PRDProperty;
import prototypes.jaxb.PRDWorld;

import java.util.Objects;

public abstract class SetValidator {
    public static boolean validate(PRDWorld world, PRDAction action) throws Exception {
        PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), action.getProperty());

        if (Objects.isNull(property))
            throw new PropertyNotFoundException(String.format("Action [%s]: Entity [%s]: Property [%s] does not exist",
                    action.getType(), action.getEntity(), action.getProperty()));

        if (!ValidatorsUtils.validateExpressionType(world, action, property.getType(), action.getValue()))
            throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: Arithmetic operation must receive arithmetic args",
                    action.getType(), action.getEntity()));

        return true;
    }
}
