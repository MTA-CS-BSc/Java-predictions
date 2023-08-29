package engine.simulation.performers;

import engine.consts.ActionTypes;
import engine.consts.PropTypes;
import engine.exceptions.EntityNotFoundException;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.InvalidTypeException;
import engine.logs.EngineLoggers;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import helpers.TypesUtils;

import java.util.Objects;

public abstract class ActionsPerformer {
    public static void fireAction(World world, Action action, SingleEntity on) throws Exception {
        if (!ActionTypes.ENTITY_MAY_NOT_EXIST_TYPES.contains(action.getType()) && !validateEntityExists(world, action))
            return;

        switch (action.getType()) {
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
    private static boolean validateEntityExists(World world, Action action) throws EntityNotFoundException {
        Entity entity = Utils.findEntityByName(world, action.getEntityName());

        if (Objects.isNull(entity))
            throw new EntityNotFoundException(String.format("Action [%s]: Entity [%s] not found!",
                    action.getType(), action.getEntityName()));

        else if (entity.getPopulation() == 0) {
            EngineLoggers.SIMULATION_LOGGER.info(String.format("Action [%s]: Entity [%s] has 0 population, skipping action...",
                    action.getType(), action.getEntityName()));
            return false;
        }

        return true;

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
        newValue = Utils.removeExtraZeroes(property, newValue);

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
