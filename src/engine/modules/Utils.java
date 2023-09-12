package engine.modules;

import dtos.PropertyDTO;
import engine.prototypes.implemented.*;
import helpers.Constants;
import helpers.types.PropTypes;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Utils {
    public static Entity findEntityByName(World world, String entityName) {
        return world.getEntities().getEntitiesMap().get(entityName);
    }
    public static Property findAnyPropertyByName(World world, String entityName, String propertyName) {
        Entity mainEntity = Utils.findEntityByName(world, entityName);
        return mainEntity.getInitialProperties().getPropsMap().get(propertyName);
    }
    public static Property findPropertyByName(SingleEntity entity, String propertyName) {
        return entity.getProperties().getPropsMap().get(propertyName);
    }
    public static Property findEnvironmentPropertyByName(World world, String propertyName) {
        return world.getEnvironment().getEnvVars().values()
                .stream()
                .filter(element -> element.getName().equals(propertyName))
                .findFirst().orElse(null);
    }
    public static void setPropRandomInit(Property property, Range range) {
        switch (property.getType()) {
            case PropTypes.BOOLEAN:
                property.getValue().setInit(String.valueOf(RandomGenerator.randomizeRandomBoolean()));
                break;
            case PropTypes.DECIMAL:
                int intFrom = Objects.isNull(range) ? Constants.MIN_RANGE : (int)range.getFrom();
                int intTo = Objects.isNull(range) ? Constants.MAX_RANGE : (int)range.getTo();

                property.getValue().setInit(String.valueOf(RandomGenerator.randomizeRandomNumber(intFrom, intTo)));
                break;
            case PropTypes.FLOAT:
                float floatFrom = Objects.isNull(range) ? (float)Constants.MIN_RANGE : (float)range.getFrom();
                float floatTo = Objects.isNull(range) ? (float)Constants.MAX_RANGE : (float)range.getTo();

                property.getValue().setInit(String.valueOf(RandomGenerator.randomizeFloat(floatFrom, floatTo)));
                break;
            case PropTypes.STRING:
                property.getValue().setInit(RandomGenerator.randomizeRandomString());
                break;
        }

        property.getValue().setCurrentValue(property.getValue().getInit());
    }
    public static boolean validateValueInRange(Property property, String newValue) {
        if (!Objects.isNull(property.getRange()))
            return !(Float.parseFloat(newValue) > property.getRange().getTo())
                    && !(Float.parseFloat(newValue) < property.getRange().getFrom());

        return true;
    }
    public static boolean validateValueInRange(PropertyDTO property, String newValue) {
        if (!Objects.isNull(property.getRange()))
            return !(Float.parseFloat(newValue) > property.getRange().getTo())
                    && !(Float.parseFloat(newValue) < property.getRange().getFrom());

        return true;
    }
    public static boolean isPossibleToPerformRule(Map<String, Rule> allRules, String ruleName, long ticks) {
        float randomProbability = RandomGenerator.randomizeProbability();
        Rule rule = allRules.get(ruleName);

        return rule.getActivation().getProbability() >= randomProbability
                && ticks % rule.getActivation().getTicks() == 0;
    }
    public static Map<String, Rule> getRulesToApply(World world, long ticks) {
        Map<String, Rule> allRules = world.getRules().getRulesMap();

        return allRules.values()
                .stream()
                .filter(element -> Utils.isPossibleToPerformRule(allRules, element.getName(), ticks))
                .collect(Collectors.toMap(
                        Rule::getName,
                        rule -> rule
                ));

    }
}
