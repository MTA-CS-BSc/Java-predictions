package engine.simulation.performers;
import engine.consts.*;
import engine.exceptions.PropertyNotFoundException;
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
            if (type.equalsIgnoreCase(ActionTypes.INCREASE))
                IncrementPerformer.handle(world, action, on);

            else if (type.equalsIgnoreCase(ActionTypes.DECREASE))
                DecrementPerformer.handle(world, action, on);

            else if (type.equalsIgnoreCase(ActionTypes.CALCULATION))
                CalculationPerformer.handle(world, action, on);

            else if (type.equalsIgnoreCase(ActionTypes.SET))
                SetPerformer.handle(world, action, on);

            else if (type.equalsIgnoreCase(ActionTypes.KILL))
                KillPerformer.handle(world, action, on);

            else if (type.equalsIgnoreCase(ActionTypes.CONDITION))
                ConditionPerformer.handle(world, action, on);
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
                                         Property property, String newValue) {
        property.getValue().setCurrentValue(newValue);
        property.setStableTime(0);

        Loggers.SIMULATION_LOGGER.info(String.format("Action [%s]: Entity [%s]: Property [%s]: value changed to [%s]",
                actionType, entityName, property.getName(), newValue));
    }
}
