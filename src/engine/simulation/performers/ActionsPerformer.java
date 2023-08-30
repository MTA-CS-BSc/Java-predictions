package engine.simulation.performers;

import engine.consts.ActionTypes;
import engine.consts.PropTypes;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.InvalidTypeException;
import engine.logs.EngineLoggers;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.*;
import helpers.TypesUtils;

public abstract class ActionsPerformer {
    public static void fireAction(World world, Action action, SingleEntity on) throws Exception {
        //TODO: Add secondary entity chooser & send to all functions
        switch (action.getType()) {
            case ActionTypes.INCREASE:
                IncreasePerformer.handle(world, (IncreaseAction)action, on);
                break;
            case ActionTypes.DECREASE:
                DecreasePerformer.handle(world, (DecreaseAction)action, on);
                break;
            case ActionTypes.CALCULATION:
                CalculationPerformer.handle(world, (CalculationAction)action, on);
                break;
            case ActionTypes.SET:
                SetPerformer.handle(world, (SetAction)action, on);
                break;
            case ActionTypes.KILL:
                KillPerformer.handle(world, (KillAction)action, on);
                break;
            case ActionTypes.CONDITION:
                ConditionPerformer.handle(world, (ConditionAction)action, on);
                break;
            case ActionTypes.PROXIMITY:
                ProximityPerformer.handle(world, (ProximityAction)action, on);
                break;
            case ActionTypes.REPLACE:
                ReplacePerformer.handle(world, (ReplaceAction)action, on);
                break;
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
        if (PropTypes.NUMERIC_PROPS.contains(property.getType()))
            newValue = Utils.removeExtraZeroes(newValue);

        if (TypesUtils.isFloat(newValue) && !TypesUtils.isDecimal(newValue) && property.getType().equals(PropTypes.DECIMAL))
            throw new InvalidTypeException(String.format("Action [%s]: Entity: [%s]: Property [%s]: [%s] is not decimal and therefore is not set",
                    actionType, entityName, property.getName(), newValue));

        if (!Utils.validateValueInRange(property, newValue)) {
            EngineLoggers.SIMULATION_LOGGER.info(ErrorMessageFormatter.formatActionErrorMessage(
                    actionType, entityName, property.getName(),
                    String.format("value [%s] not in range and therefore is not set", newValue)));

            return;
        }

        property.getValue().setCurrentValue(newValue);
        property.setStableTime(0);

        EngineLoggers.SIMULATION_LOGGER.info(String.format("Action [%s]: Entity [%s]: Property [%s]: value changed to [%s]",
                actionType, entityName, property.getName(), newValue));
    }
}
