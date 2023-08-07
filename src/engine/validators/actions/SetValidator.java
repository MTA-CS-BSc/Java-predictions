package engine.validators.actions;

import engine.consts.PropTypes;
import engine.exceptions.InvalidTypeException;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.ValidatorsUtils;
import engine.parsers.ValidationExpressionParser;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

public class SetValidator {
    public static boolean validate(PRDWorld world, PRDAction action) throws PropertyNotFoundException, InvalidTypeException {
        PRDProperty property = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), action.getProperty());

        if (Objects.isNull(property))
            throw new PropertyNotFoundException(String.format("Action [%s]: Entity [%s]: Property [%s] does not exist",
                    action.getType(), action.getEntity(), action.getProperty()));

        String propertyType = property.getType();
        String parsedExpression = ValidationExpressionParser.parseExpression(world, action, action.getValue());
        String parsedExpressionType = ValidationExpressionParser.getExpressionType(world, action, parsedExpression);

        if (PropTypes.NUMERIC_PROPS.contains(propertyType)) {
            if (!PropTypes.NUMERIC_PROPS.contains(parsedExpressionType))
                throw new InvalidTypeException(String.format("Action [%s]: Entity: [%s]: value [%s] is not numeric",
                        action.getType(), action.getEntity(), action.getValue()));

            return true;
        }

        else if (PropTypes.BOOLEAN_PROPS.contains(propertyType)) {
            if (PropTypes.BOOLEAN_PROPS.contains(parsedExpressionType))
                throw new InvalidTypeException(String.format("Action [%s]: Entity [%s]: value [%s] is not boolean",
                        action.getType(), action.getEntity(), action.getValue()));

            return true;
        }

        return true;
    }

}
