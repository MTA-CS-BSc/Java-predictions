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
    private static void handleAll(Entity mainEntity, String propertyName, String by) {
        mainEntity.getSingleEntities().forEach(entity -> {
            try {
                handleSingle(mainEntity, entity, propertyName, by);
            } catch (ValueNotInRangeException e) {
                Loggers.SIMULATION_LOGGER.info(e.getMessage());
            }
        });
    }
    private static void handleSingle(Entity mainEntity, SingleEntity on, String propertyName, String by) throws ValueNotInRangeException {
        Property propToChange = Utils.findPropertyByName(on, propertyName);
        String newValue = getIncrementResult(propToChange.getValue().getCurrentValue(), by);

        if (!Utils.validateValueInRange(propToChange, newValue))
            throw new ValueNotInRangeException(ErrorMessageFormatter.formatActionErrorMessage(
                    "Increase", mainEntity.getName(), propertyName,
                    String.format("value [%s] not in range and therefore is not set", newValue)));

        propToChange.getValue().setCurrentValue(newValue);
        propToChange.setStableTime(0);
    }
    private static void performAction(World world, Action action, SingleEntity on) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());
        String parsedValue = ExpressionParser.parseExpression(world, action, action.getBy());
        String by = ExpressionParser.evaluateExpression(parsedValue, on);

        if (Objects.isNull(on))
            handleAll(mainEntity, action.getPropertyName(), by);

        else {
            try {
                handleSingle(mainEntity, on, action.getPropertyName(), by);
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
