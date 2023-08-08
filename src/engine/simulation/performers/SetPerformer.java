package engine.simulation.performers;

import engine.consts.PropTypes;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.PropertyNotFoundException;
import engine.exceptions.ValueNotInRangeException;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.prototypes.implemented.*;

import java.util.Objects;

public class SetPerformer {
    private static void handleAll(World world, Action action) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        mainEntity.getSingleEntities().forEach(entity -> {
            try {
                handleSingle(world, action, entity);
            } catch (ValueNotInRangeException e) {
                Loggers.SIMULATION_LOGGER.info(e.getMessage());
            }
        });
    }
    private static void handleSingle(World world, Action action, SingleEntity on) throws ValueNotInRangeException {
//        Property property = Utils.findPropertyByName(on, action.getPropertyName());
//        String parsedValue = ExpressionParser.parseExpression(world, action.getEntityName(), action.getValue(), on);
//        String newValue = ExpressionParser.evaluateExpression(parsedValue, on);
//
//        if (PropTypes.NUMERIC_PROPS.contains(property.getType()))
//            if (!Utils.validateValueInRange(property, newValue))
//                throw new ValueNotInRangeException(ErrorMessageFormatter.formatActionErrorMessage(
//                        "Set", action.getEntityName(), action.getPropertyName(),
//                        String.format("value [%s] not in range and therefore is not set", newValue)));
//
//        ActionsPerformer.setPropertyValue("Set", action.getEntityName(), property, newValue);
    }
    private static void performAction(World world, Action action, SingleEntity on) {
        if (Objects.isNull(on))
            handleAll(world, action);

        else {
            try {
                handleSingle(world, action, on);
            } catch (ValueNotInRangeException e) {
                Loggers.SIMULATION_LOGGER.info(e.getMessage());
            }
        }
    }
    public static void handle(World world, Action action, SingleEntity on) throws PropertyNotFoundException {
        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getPropertyName()));

        performAction(world, action, on);
    }
}
