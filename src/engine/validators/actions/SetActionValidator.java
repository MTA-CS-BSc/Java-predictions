package engine.validators.actions;

import engine.consts.PropTypes;
import engine.consts.SystemFunctions;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.Utils;
import engine.modules.ValidatorsUtils;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDEnvProperty;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public class SetActionValidator {
    public static boolean validate(PRDWorld world, PRDAction action) throws PropertyNotFoundException {
        PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), action.getProperty());

        if (Objects.isNull(property))
            throw new PropertyNotFoundException(String.format("Action [%s]: Entity [%s]: Property [%s] does not exist",
                    action.getType(), action.getEntity(), action.getProperty()));

        return ValidatorsUtils.validateExpressionType(world, action, property);
    }
}
