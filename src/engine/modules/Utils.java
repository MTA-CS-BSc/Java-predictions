package engine.modules;

import dtos.PropertyDTO;
import helpers.ActionTypes;
import helpers.PropTypes;
import engine.consts.SystemFunctions;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.Action;
import helpers.Constants;
import helpers.TypesUtils;

import java.util.*;
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
    public static String removeExtraZeroes(String value) {
        return value.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT) ? value.split("\\.")[0] : value;
    }
    public static String getSystemFunctionType(String expression) {
        return expression.substring(0, expression.indexOf("("));
    }
    public static String getExpressionType(World world, String entityName, String expression) {
        Property expressionEntityProp = findAnyPropertyByName(world, entityName, expression);

        if (ValidatorsUtils.isSystemFunction(expression)) {
            String systemFunctionValue = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

            switch (getSystemFunctionType(expression)) {
                case SystemFunctions.RANDOM:
                case SystemFunctions.TICKS:
                    return PropTypes.DECIMAL;
                case SystemFunctions.PERCENT:
                    return PropTypes.FLOAT;
                case SystemFunctions.ENVIRONMENT:
                    return findEnvironmentPropertyByName(world, systemFunctionValue).getType();
                case SystemFunctions.EVALUATE:
                    String evaluateEntityName = systemFunctionValue.split("\\.")[0];
                    String evaluatePropName = systemFunctionValue.split("\\.")[1];
                    return findAnyPropertyByName(world, evaluateEntityName, evaluatePropName).getType();
            }
        }

        else if (!Objects.isNull(expressionEntityProp))
            return expressionEntityProp.getType();

        else if (TypesUtils.isDecimal(expression))
            return PropTypes.DECIMAL;

        else if (TypesUtils.isFloat(expression))
            return PropTypes.FLOAT;

        else if (TypesUtils.isBoolean(expression))
            return PropTypes.BOOLEAN;

        return PropTypes.STRING;
    }
    public static List<Action> getOrderedActionsList(List<Action> actions) {
        List<Action> result = new ArrayList<>();
        List<Action> killActions = new ArrayList<>();

        for (Action action : actions) {
            if (!action.getType().equals(ActionTypes.REPLACE) && !action.getType().equals(ActionTypes.KILL))
                result.add(action);

            else
                killActions.add(action);

        }

        result.addAll(killActions);
        return result;
    }
}
