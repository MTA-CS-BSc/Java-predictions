package engine.simulation.performers;
import engine.consts.*;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.InvalidTypeException;
import engine.exceptions.PropertyNotFoundException;
import engine.exceptions.ValueNotInRangeException;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.prototypes.implemented.*;

import java.util.Objects;

public class ActionsPerformer {
    public static void fireAction(World world, Action action, SingleEntity on) {
        if (Objects.isNull(Utils.findEntityByName(world, action.getEntityName())))
            return;

        String type = action.getType();

        try {
            switch (type) {
                case ActionTypes.INCREASE:
                    IncrementPerformer.handle(world, action, on);
                    break;
                case ActionTypes.DECREASE:
                    DecrementPerformer.handle(world, action, on);
                    break;
                case ActionTypes.CALCULATION:
                    CalculationPerformer.handle(world, action, on);
                    break;
                case ActionTypes.SET:
                    SetPerformer.handle(world, action, on);
                    break;
                case ActionTypes.KILL:
                    KillPerformer.handle(world, action, on);
                    break;
                case ActionTypes.CONDITION:
                    ConditionPerformer.handle(world, action, on);
                    break;
            }
        }

        catch (Exception e) {
            Loggers.SIMULATION_LOGGER.info(String.format("Action [%s]: Entity [%s]: Property not found. Skipping action...",
                    type, action.getEntityName()));
        }
    }
    public static void updateStableTimeToAllProps(World world) {
        world.getEntities().getEntitiesMap().values().forEach(entity -> {
            entity.getSingleEntities().forEach(singleEntity -> {
                singleEntity.getProperties().getPropsMap().forEach((key, prop) -> {
                    prop.setStableTime(prop.getStableTime() + 1);
                });
            });
        });
    }
    public static void setPropertyValue(String actionType, String entityName,
                                         Property property, String newValue) throws Exception {
        if (newValue.matches(Utils.REGEX_ONLY_ZEROES_AFTER_DOT))
            newValue = newValue.split("\\.")[0];

        else if (Utils.isFloat(newValue) && !Utils.isDecimal(newValue) && property.getType().equals(PropTypes.DECIMAL))
            throw new InvalidTypeException(String.format("Action [%s]: Entity: [%s]: Property [%s]: [%s] is not decimal and therefore is not set",
                    actionType, entityName, property.getName(), newValue));

        if (!Utils.validateValueInRange(property, newValue))
            throw new ValueNotInRangeException(ErrorMessageFormatter.formatActionErrorMessage(
                    actionType, entityName, property.getName(),
                    String.format("value [%s] not in range and therefore is not set", newValue)));

        property.getValue().setCurrentValue(newValue);
        property.setStableTime(0);

        Loggers.SIMULATION_LOGGER.info(String.format("Action [%s]: Entity [%s]: Property [%s]: value changed to [%s]",
                actionType, entityName, property.getName(), newValue));
    }
}
