package dtos;

import engine.prototypes.implemented.*;
import engine.simulation.SingleSimulation;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Mappers {
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

        return new WorldDTO(entities, stopConditions, rules, envs);
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
                rule.getActivation().getProbability(), rule.getActions().getActions().size());
    }
    public static RangeDTO toDto(Range range) {
        return !Objects.isNull(range) ? new RangeDTO(range.getFrom(), range.getTo()) : null;
    }
}
