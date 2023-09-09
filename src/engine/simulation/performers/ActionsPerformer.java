package engine.simulation.performers;

import engine.consts.SecondaryEntityCounts;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.InvalidTypeException;
import engine.logs.EngineLoggers;
import engine.modules.RandomGenerator;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.*;
import helpers.PropTypes;
import helpers.TypesUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class ActionsPerformer {
    public static void fireAction(World world, Action actToPerform, SingleEntity main) throws Exception {
        if (!Objects.isNull(actToPerform.getSecondaryEntity())) {
            List<SingleEntity> secondaryEntities = ActionsPerformer.chooseSecondaryEntities(world, actToPerform);

            //TODO: Check if should skip action
            if (secondaryEntities.isEmpty())
                return;

            for (SingleEntity currentSecondary : secondaryEntities)
                ActionsPerformer.handleAction(world, actToPerform, main, currentSecondary);
        }

        else
            ActionsPerformer.handleAction(world, actToPerform, main, null);
    }
    private static void handleAction(World world, Action action, SingleEntity main, SingleEntity secondary) throws Exception {
        if (action instanceof IncreaseAction)
            IncreasePerformer.performAction(world, (IncreaseAction)action, main, secondary);

        else if (action instanceof DecreaseAction)
            DecreasePerformer.performAction(world, (DecreaseAction)action, main, secondary);

        else if (action instanceof CalculationAction)
            CalculationPerformer.performAction(world, (CalculationAction)action, main, secondary);

        else if (action instanceof SetAction)
            SetPerformer.performAction(world, (SetAction)action, main, secondary);

        else if (action instanceof ConditionAction)
            ConditionPerformer.performAction(world, (ConditionAction)action, main, secondary);

        else if (action instanceof KillAction)
            KillPerformer.performAction(world, (KillAction)action, main, secondary);

        else if (action instanceof ProximityAction)
            ProximityPerformer.performAction(world, (ProximityAction)action, main, secondary);

        else if (action instanceof ReplaceAction)
            ReplacePerformer.performAction(world, (ReplaceAction)action, main);
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
    public static List<SingleEntity> chooseSecondaryEntities(World world, Action action) throws Exception {
        Condition condition = action.getSecondaryEntity().getSelection().getCondition();
        Entity secondaryEntity = Utils.findEntityByName(world, action.getSecondaryEntity().getEntityName());
        String count = action.getSecondaryEntity().getSelection().getCount();
        List<SingleEntity> conditionSecondaryEntities = new ArrayList<>();
        List<SingleEntity> returned = new ArrayList<>();

        for (SingleEntity current : secondaryEntity.getSingleEntities())
            if (ConditionPerformer.evaluateCondition(world, condition, current, null))
                conditionSecondaryEntities.add(current);

        if (count.equals(SecondaryEntityCounts.ALL))
            return conditionSecondaryEntities;

        else if (TypesUtils.isDecimal(count)) {
            int amount = Integer.parseInt(count);

            if (amount > conditionSecondaryEntities.size())
                amount = conditionSecondaryEntities.size();

            for (int i = 0; i < amount; i++)
                returned.add(conditionSecondaryEntities.get(RandomGenerator.randomizeRandomNumber(0, conditionSecondaryEntities.size())));

            return returned;
        }

        return Collections.emptyList();
    }
}
