package engine.validators.actions;

import engine.exceptions.InvalidTypeException;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.ValidatorsUtils;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public class SetValidator {
    public static boolean validate(PRDWorld world, PRDAction action) throws Exception {
        PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), action.getProperty());

        if (Objects.isNull(property))
            throw new PropertyNotFoundException(String.format("Action [%s]: Entity [%s]: Property [%s] does not exist",
                    action.getType(), action.getEntity(), action.getProperty()));

        if (!ValidatorsUtils.validateExpressionType(world, action, property, action.getValue()))
            throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: Arithmetic operation must receive arithmetic args",
                    action.getType(), action.getEntity()));

        return true;
    }
}
