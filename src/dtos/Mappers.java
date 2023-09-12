package dtos;

import dtos.actions.*;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.*;
import engine.simulation.SingleSimulation;
import helpers.types.CalculationTypes;
import helpers.loggers.ConditionSingularities;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Mappers {
    public static SingleSimulationDTO toDto(SingleSimulation simulation) {
        return new SingleSimulationDTO(simulation.getUUID(), simulation.getStartTimestamp(),
                toDto(simulation.getWorld()));
    }
    public static WorldDTO toDto(World world) {
        List<EntityDTO> entities = world.getEntities().getEntitiesMap().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(EntityDTO::getName))
                .collect(Collectors.toList());

        List<StopConditionDTO> stopConditions = world.getTermination().getStopConditions()
                .stream()
                .map(Mappers::toDto)
                .collect(Collectors.toList());

        TerminationDTO termination = new TerminationDTO(stopConditions, world.getTermination().isStopByUser());

        List<PropertyDTO> envs = world.getEnvironment().getEnvVars().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(PropertyDTO::getName))
                .collect(Collectors.toList());

        List<RuleDTO> rules = world.getRules().getRulesMap().values()
                .stream()
                .map(Mappers::toDto)
                .sorted(Comparator.comparing(RuleDTO::getName))
                .collect(Collectors.toList());

        return new WorldDTO(entities, termination, rules, envs, world.getGrid().getRows(), world.getGrid().getColumns());
    }
    public static EntityDTO toDto(Entity entity) {
        List<PropertyDTO> entityProps = entity.getInitialProperties().getPropsMap()
                .values()
                .stream()
                .map(Mappers::toDto)
                .collect(Collectors.toList());

        return new EntityDTO(entity.getName(), entity.getPopulation(), entityProps);
    }
    public static PropertyDTO toDto(Property property) {
        String value = !Objects.isNull(property.getValue().getCurrentValue()) ?
                property.getValue().getCurrentValue() : property.getValue().getInit();

        return new PropertyDTO(property.getName(), property.getType(), toDto(property.getRange()), value, property.getValue().isRandomInitialize());
    }
    public static StopConditionDTO toDto(Object stopCondition) {
        if (stopCondition.getClass() == ByTicks.class)
            return new StopConditionDTO("ticks", ((ByTicks)stopCondition).getCount());

        return new StopConditionDTO("seconds", ((BySecond)stopCondition).getCount());
    }
    public static RuleDTO toDto(Rule rule) {
        return new RuleDTO(rule.getName(), rule.getActivation().getTicks(),
                rule.getActivation().getProbability(), toDto(rule.getActions().getActions()));
    }
    public static RangeDTO toDto(Range range) {
        return !Objects.isNull(range) ? new RangeDTO(range.getFrom(), range.getTo()) : null;
    }
    public static List<ActionDTO> toDto(List<Action> actions) {
        return actions.stream().map(Mappers::toDto).collect(Collectors.toList());
    }
    public static ActionDTO toDto(Action action) {
        SecondaryEntityDTO secondaryEntity = toDto(action.getSecondaryEntity());
        String entityName = action.getEntityName();

        if (action instanceof SetAction)
            return new SetDTO(entityName, secondaryEntity,
                    ((SetAction)action).getPropertyName(), ((SetAction)action).getValue());

        else if (action instanceof KillAction)
            return new KillDTO(entityName, secondaryEntity);

        else if (action instanceof DecreaseAction)
            return new IncreaseDecreaseDTO(action.getType(), entityName, secondaryEntity,
                    ((DecreaseAction)action).getPropertyName(), ((DecreaseAction)action).getBy());

        else if (action instanceof IncreaseAction)
            return new IncreaseDecreaseDTO(action.getType(), entityName, secondaryEntity,
                    ((IncreaseAction)action).getPropertyName(), ((IncreaseAction)action).getBy());

        else if (action instanceof ReplaceAction)
            return new ReplaceDTO(secondaryEntity,((ReplaceAction)action).getKill(),
                    ((ReplaceAction)action).getCreate(), ((ReplaceAction)action).getMode());

        else if (action instanceof ProximityAction)
            return new ProximityDTO(secondaryEntity, ((ProximityAction)action).getBetween().getSourceEntity(),
                    ((ProximityAction)action).getBetween().getTargetEntity(),
                    ((ProximityAction)action).getDepthExpression(), ((ProximityAction)action).getActions().getActions().size());

        else if (action instanceof CalculationAction) {
            String arg1 = ((CalculationAction)action).getOperationType().equals(CalculationTypes.MULTIPLY) ?
                    ((CalculationAction)action).getMultiply().getArg1() : ((CalculationAction)action).getDivide().getArg1();
            String arg2 = ((CalculationAction)action).getOperationType().equals(CalculationTypes.MULTIPLY) ?
                    ((CalculationAction)action).getMultiply().getArg2() : ((CalculationAction)action).getDivide().getArg2();
            return new CalculationDTO(entityName, secondaryEntity,
                    ((CalculationAction)action).getOperationType(), arg1, arg2);
        }

        else if (action instanceof ConditionAction) {
            boolean elseExists = !Objects.isNull(((ConditionAction)action).getElse());
            int thenActionsAmount = ((ConditionAction)action).getThen().getActions().size();
            int elseActionsAmount = elseExists ? ((ConditionAction)action).getElse().getActions().size() : 0;
            Condition condition = ((ConditionAction)action).getCondition();

            if (((ConditionAction)action).getCondition().getSingularity().equals(ConditionSingularities.SINGLE))
                return new SingleConditionDTO(entityName, secondaryEntity,
                        thenActionsAmount, elseActionsAmount,
                        condition.getOperator(), condition.getProperty(), condition.getValue());

            return new MultipleConditionDTO(entityName, secondaryEntity,
                    thenActionsAmount, elseActionsAmount,
                    condition.getLogicalOperator(), condition.getConditions().size());
        }

        return null;
    }
    public static SecondaryEntityDTO toDto(SecondaryEntity secondaryEntity) {
        if (Objects.isNull(secondaryEntity))
            return null;

        return new SecondaryEntityDTO(secondaryEntity.getEntityName());
    }
}
