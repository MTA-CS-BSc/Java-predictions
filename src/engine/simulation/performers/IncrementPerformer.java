package engine.simulation.performers;

import engine.exceptions.EntityNotFoundException;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.PropertyNotFoundException;
import engine.exceptions.ValueNotInRangeException;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.Objects;

public class IncrementPerformer {
    private static String getIncrementResult(String propValue, String by) {
        return String.valueOf(Float.parseFloat(propValue) - Float.parseFloat(by));
    }
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
        Property propToChange = Utils.findPropertyByName(on, action.getPropertyName());
        String parsedValue = ExpressionParser.parseExpression(world, action, action.getBy());
        String by = ExpressionParser.evaluateExpression(parsedValue, on);
        String newValue = getIncrementResult(propToChange.getValue().getCurrentValue(), by);

        if (!Utils.validateValueInRange(propToChange, newValue))
            throw new ValueNotInRangeException(ErrorMessageFormatter.formatActionErrorMessage(
                    "Decrease", action.getEntityName(), action.getPropertyName(),
                    String.format("value [%s] not in range and therefore is not set", newValue)));

        ActionsPerformer.setPropertyValue("Decrease", action.getEntityName(), propToChange, newValue);
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
    public static void handle(World world, Action action, SingleEntity on) throws EntityNotFoundException, PropertyNotFoundException {
        if (Objects.isNull(Utils.findEntityByName(world, action.getEntityName())))
            throw new EntityNotFoundException(ErrorMessageFormatter.formatEntityNotFoundMessage(action.getType(), action.getEntityName()));

        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getPropertyName()));

        performAction(world, action, on);
    }
}
