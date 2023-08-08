package engine.modules;

import engine.consts.BoolPropValues;
import engine.consts.PropTypes;
import engine.consts.Restrictions;
import engine.exceptions.ValueNotInRangeException;
import engine.logs.Loggers;
import engine.prototypes.implemented.*;
import engine.prototypes.jaxb.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    public static String REGEX_ONLY_ZEROES_AFTER_DOT = "\"^\\\\d+\\\\.0+$\"";
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
    public static void setPropRandomInit(Property property, Range range) {
        switch (property.getType()) {
            case PropTypes.BOOLEAN:
                property.getValue().setInit(String.valueOf(RandomGenerator.randomizeRandomBoolean()));
                break;
            case PropTypes.DECIMAL:
                property.getValue().setInit(String.valueOf(RandomGenerator.randomizeRandomNumber((int) range.getFrom(), (int) range.getTo())));
                break;
            case PropTypes.FLOAT:
                property.getValue().setInit(String.valueOf(RandomGenerator.randomizeFloat((float) range.getFrom(), (float) range.getTo())));
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
    public static boolean isPossibleToPerformRule(HashMap<String, Rule> allRules, String ruleName, long ticks) {
        float randomProbability = RandomGenerator.randomizeProbability();
        Rule rule = allRules.get(ruleName);

        return rule.getActivation().getProbability() >= randomProbability
                && ticks % rule.getActivation().getTicks() == 0;
    }
    public static Map<String, Rule> getRulesToApply(World world, long ticks) {
        HashMap<String, Rule> allRules = world.getRules().getRulesMap();

        return allRules.values()
                .stream()
                .filter(element -> Utils.isPossibleToPerformRule(allRules, element.getName(), ticks))
                .collect(Collectors.toMap(
                        Rule::getName,
                        rule -> rule
                ));

    }
    public static boolean isDecimal(String str) {
        try {
            Integer.parseInt(str);
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }

    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }
    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss").format(date);
    }

    public static boolean isBoolean(String str) {
        return str.equalsIgnoreCase(BoolPropValues.TRUE) || str.equalsIgnoreCase(BoolPropValues.FALSE);
    }
}
