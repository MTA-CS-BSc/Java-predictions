package engine.simulation.performers;

import engine.consts.PropTypes;
import engine.exceptions.EntityNotFoundException;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.PropertyNotFoundException;
import engine.exceptions.ValueNotInRangeException;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.Objects;

public class SetPerformer {
    private static void handleAll(Entity mainEntity, String propertyName, String newValue) {
        mainEntity.getSingleEntities().forEach(entity -> {
            try {
                handleSingle(entity, mainEntity.getName(), propertyName, newValue);
            } catch (ValueNotInRangeException e) {
                Loggers.SIMULATION_LOGGER.info(e.getMessage());
            }
        });
    }
    private static void handleSingle(SingleEntity on, String entityName,
                                     String propertyName, String newValue) throws ValueNotInRangeException {
        Property property = Utils.findPropertyByName(on, propertyName);

        if (PropTypes.NUMERIC_PROPS.contains(property.getType()))
            if (!Utils.validateValueInRange(property, newValue))
                throw new ValueNotInRangeException(ErrorMessageFormatter.formatActionErrorMessage(
                        "Set", entityName, propertyName,
                        String.format("value [%s] not in range and therefore is not set", newValue)));

        ActionsPerformer.setPropertyValue("Set", entityName, property, newValue);
    }
    private static void performAction(World world, Action action, SingleEntity on) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());
        String parsedValue = ExpressionParser.parseExpression(world, action, action.getValue());
        String newValue = ExpressionParser.evaluateExpression(parsedValue, on);

        if (Objects.isNull(on))
            handleAll(mainEntity, action.getPropertyName(), newValue);

        else {
            try {
                handleSingle(on, mainEntity.getName(), action.getPropertyName(), newValue);
            } catch (ValueNotInRangeException e) {
                Loggers.SIMULATION_LOGGER.info(e.getMessage());
            }

        }
    }
    public static void handle(World world, Action action, SingleEntity on) throws EntityNotFoundException, PropertyNotFoundException {
        if (Objects.isNull(Utils.findEntityByName(world, action.getEntityName())))
            throw new EntityNotFoundException(ErrorMessageFormatter.formatEntityNotFoundMessage(action.getType(), action.getEntityName()));

        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getPropertyName()));

        performAction(world, action, on);
    }
}
