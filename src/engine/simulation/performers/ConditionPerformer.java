package engine.simulation.performers;

import engine.consts.ConditionLogicalOperators;
import engine.consts.ConditionSingularities;
import engine.consts.Operators;
import engine.exceptions.EntityNotFoundException;
import engine.exceptions.InvalidTypeException;
import engine.exceptions.PropertyNotFoundException;
import engine.logs.EngineLoggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;
import helpers.Constants;
import helpers.TypesUtils;

import java.util.List;
import java.util.Objects;

public abstract class ConditionPerformer {
    private static boolean evaluateSingleCondition(World world, Action action,
                                                   Condition condition, SingleEntity on) throws Exception {
        Property property = Utils.findPropertyByName(on, condition.getProperty());

        if (Objects.isNull(Utils.findEntityByName(world, condition.getEntityName())))
            throw new EntityNotFoundException(String.format("Action: [%s]: Entity [%s] not found",
                    action.getType(), action.getEntityName()));

        else if (Objects.isNull(property) || Objects.isNull(property.getValue().getCurrentValue()))
            throw new PropertyNotFoundException(String.format("Entity [%s] or property [%s] do not exist",
                    condition.getEntityName(), condition.getProperty()));

        String value = ExpressionParser.evaluateExpression(world, action, condition.getValue(), on);
        String operator = condition.getOperator();

        return getConditionResult(property, operator, value);
    }
    private static boolean getConditionResult(Property property, String operator, String value) throws InvalidTypeException {
        boolean isNumeric = TypesUtils.isDecimal(value) || TypesUtils.isFloat(value);

        switch (operator) {
            case Operators.BT:
                if (isNumeric)
                    return Float.parseFloat(property.getValue().getCurrentValue())
                            > Float.parseFloat(value);

                else
                    throw new InvalidTypeException("LT & BT are only for numeric values");
                case Operators.LT:
                if (isNumeric)
                    return Float.parseFloat(property.getValue().getCurrentValue())
                            < Float.parseFloat(value);

                else
                    throw new InvalidTypeException("LT & BT are only for numeric values");
            case Operators.EQUALS:
                if (value.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT))
                    value = value.split("\\.")[0];

                return value.equals(property.getValue().getCurrentValue());
            case Operators.NOT_EQUALS:
                if (value.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT))
                    value = value.split("\\.")[0];

                return !value.equals(property.getValue().getCurrentValue());
        }

        return true;
    }
    private static boolean evaluateMultipleCondition(World world, Action action,
                                                     Condition condition, SingleEntity on) {
        List<Condition> allConditions = condition.getConditions();
        String logicalOperator = condition.getLogicalOperator();

        if (logicalOperator.equalsIgnoreCase(ConditionLogicalOperators.AND))
            return allConditions.stream()
                     .allMatch(current -> {
                         try {
                             return evaluateCondition(world, action, current, on);
                         } catch (Exception e) {
                             EngineLoggers.SIMULATION_LOGGER.info(e.getMessage());
                             return false;
                         }
                     });


        else if (logicalOperator.equalsIgnoreCase(ConditionLogicalOperators.OR))
            return allConditions.stream()
                    .anyMatch(current -> {
                        try {
                            return evaluateCondition(world, action, current, on);
                        } catch (Exception e) {
                            EngineLoggers.SIMULATION_LOGGER.info(e.getMessage());
                            return false;
                        }
                    });

        return true;
    }
    private static boolean evaluateCondition(World world, Action action, Condition condition, SingleEntity on) throws Exception {
        if (condition.getSingularity().equals(ConditionSingularities.SINGLE))
            return evaluateSingleCondition(world, action, condition, on);

        return evaluateMultipleCondition(world, action, condition, on);
    }
    private static void handleAll(World world, Action action) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        mainEntity.getSingleEntities().forEach(on -> {
            try {
                handleSingle(world, action, on);
            } catch (Exception e) {
                EngineLoggers.SIMULATION_LOGGER.info(e.getMessage());
            }
        });
    }
    private static void handleSingle(World world, Action action, SingleEntity on) throws Exception {
        Condition condition = action.getCondition();
        List<Action> thenActions = action.getThen().getActions();
        Else prdElse = action.getElse();
        boolean conditionResult = evaluateCondition(world, action, condition, on);

        EngineLoggers.SIMULATION_LOGGER.info(String.format("Action [%s]: Entity [%s]: Condition result is [%s]," +
                " evaluating relevant actions...", action.getType(), action.getEntityName(), conditionResult));

        if (conditionResult)
            thenActions.forEach(actToPerform -> {
                try {
                    ActionsPerformer.fireAction(world, actToPerform, on);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        else
            prdElse.getActions().forEach(actToPerform -> {
                try {
                    ActionsPerformer.fireAction(world, actToPerform, on);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
    }
    public static void handle(World world, Action action, SingleEntity on) throws Exception {
        if (Objects.isNull(on))
            handleAll(world, action);

        else
            handleSingle(world, action, on);
    }
}