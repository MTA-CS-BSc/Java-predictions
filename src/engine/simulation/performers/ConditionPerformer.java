package engine.simulation.performers;

import engine.consts.ConditionLogicalOperators;
import engine.consts.ConditionSingularities;
import engine.consts.Operators;
import engine.exceptions.InvalidTypeException;
import engine.logs.EngineLoggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.Action;
import engine.prototypes.implemented.actions.ConditionAction;
import helpers.Constants;
import helpers.TypesUtils;

import java.util.List;
import java.util.Objects;

public abstract class ConditionPerformer {
    private static boolean evaluateSingleCondition(World world, ConditionAction action,
                                                   Condition condition, SingleEntity on) throws Exception {
        Property property = Utils.findPropertyByName(on, condition.getProperty());
        String arg2 = ExpressionParser.evaluateExpression(world, action, condition.getValue(), on);
        String operator = condition.getOperator();

        if (!Objects.isNull(property))
            return getConditionResult(property.getValue().getCurrentValue(), arg2, operator);

        String arg1 = ExpressionParser.evaluateExpression(world, action, condition.getProperty(), on);
        return getConditionResult(arg1, arg2, operator);

    }
    private static boolean getConditionResult(String arg1, String arg2, String operator) throws InvalidTypeException {
        boolean isNumeric = TypesUtils.isDecimal(arg1) || TypesUtils.isFloat(arg1);

        if (isNumeric && arg2.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT))
            arg2 = Utils.removeExtraZeroes(arg2);

        switch (operator) {
            case Operators.BT:
                if (isNumeric)
                    return Float.parseFloat(arg1) > Float.parseFloat(arg2);
                else
                    throw new InvalidTypeException("lt & bt operators are only for numeric values");
                case Operators.LT:
                if (isNumeric)
                    return Float.parseFloat(arg1) < Float.parseFloat(arg2);
                else
                    throw new InvalidTypeException("lt & bt operators are only for numeric values");
            case Operators.EQUALS:
                return arg1.equalsIgnoreCase(arg2);
            case Operators.NOT_EQUALS:
                return !arg1.equalsIgnoreCase(arg2);
        }

        return true;
    }
    private static boolean evaluateMultipleCondition(World world, ConditionAction action,
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
    private static boolean evaluateCondition(World world, ConditionAction action, Condition condition, SingleEntity on) throws Exception {
        if (condition.getSingularity().equals(ConditionSingularities.SINGLE))
            return evaluateSingleCondition(world, action, condition, on);

        return evaluateMultipleCondition(world, action, condition, on);
    }
    public static void performAction(World world, ConditionAction action, SingleEntity on) throws Exception {
        Condition condition = action.getCondition();
        List<Action> thenActions = action.getThen().getActions();
        Else prdElse = action.getElse();
        boolean conditionResult = evaluateCondition(world, action, condition, on);

        EngineLoggers.SIMULATION_LOGGER.info(String.format("Action [%s]: Entity [%s]: Condition result is [%s]," +
                " evaluating relevant actions...", action.getType(), condition.getEntityName(), conditionResult));

        if (conditionResult)
            for (Action actToPerform : thenActions)
                ActionsPerformer.fireAction(world, actToPerform, on);

        else if (!Objects.isNull(prdElse))
            for (Action actToPerform : prdElse.getActions())
                ActionsPerformer.fireAction(world, actToPerform, on);
    }
}