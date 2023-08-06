package engine.simulation.performers;

import engine.consts.BoolPropValues;
import engine.consts.ConditionLogicalOperators;
import engine.consts.ConditionSingularities;
import engine.consts.Operators;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConditionPerformer {
    private static boolean evaluateSingleCondition(World world, Action action,
                                                   Condition condition, SingleEntity on) {
        Property property = Utils.findPropertyByName(on, condition.getProperty());

        if (Objects.isNull(Utils.findEntityByName(world, condition.getEntityName()))
                || Objects.isNull(property) || Objects.isNull(property.getValue())) {
            Loggers.SIMULATION_LOGGER.info(String.format("Entity [%s] or property [%s] do not exist",
                    condition.getEntityName(), condition.getProperty()));
            return true;
        }

        String parsedValue = ExpressionParser.parseExpression(world, action, condition.getValue());
        String value = ExpressionParser.evaluateExpression(parsedValue, on);
        String operator = condition.getOperator();
        boolean isNumeric = Utils.isDecimal(value);

        switch (operator) {
            case Operators.BT:
                if (isNumeric)
                    return Float.parseFloat(property.getValue().getCurrentValue())
                            > Float.parseFloat(value);
                break;
            case Operators.LT:
                if (isNumeric)
                    return Float.parseFloat(property.getValue().getCurrentValue())
                            < Float.parseFloat(value);
                break;
            case Operators.EQUALS:
                return value.equals(property.getValue().getCurrentValue());
            case Operators.NOT_EQUALS:
                return !value.equals(property.getValue().getCurrentValue());
        }

        return true;
    }
    private static boolean evaluateMultipleCondition(World world, Action action,
                                                     Condition condition, SingleEntity on) {
        List<Condition> allConditions = condition.getConditions();
        String logicalOperator = condition.getLogicalOperator();

        if (logicalOperator.equalsIgnoreCase(ConditionLogicalOperators.OR))
            return allConditions.stream()
                     .allMatch(current -> evaluateCondition(world, action, current, on));


        else if (logicalOperator.equalsIgnoreCase(ConditionLogicalOperators.AND))
            return allConditions.stream()
                    .anyMatch(current -> evaluateCondition(world, action, current, on));

        return true;
    }
    private static boolean evaluateCondition(World world, Action action, Condition condition, SingleEntity on) {
        if (condition.getSingularity().equals(ConditionSingularities.SINGLE))
            return evaluateSingleCondition(world, action, condition, on);

        return evaluateMultipleCondition(world, action, condition, on);
    }
    private static void handleAll(World world, Action action) {
        Condition condition = action.getCondition();
        List<Action> thenActions = action.getThen().getActions();
        Else prdElse = action.getElse();
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        mainEntity.getSingleEntities().forEach(on -> {
            boolean conditionResult = evaluateCondition(world, action, condition, on);

            if (conditionResult)
                thenActions.forEach(actToPerform -> ActionsPerformer.fireAction(world, actToPerform, on));

            else
                prdElse.getActions().forEach(actToPerform -> ActionsPerformer.fireAction(world, actToPerform, on));
        });
    }
    private static void handleSingle(World world, Action action, SingleEntity on) {
        Condition condition = action.getCondition();
        List<Action> thenActions = action.getThen().getActions();
        Else prdElse = action.getElse();
        
        if (evaluateCondition(world, action, condition, on))
            thenActions.forEach(actToPerform -> ActionsPerformer.fireAction(world, actToPerform, on));

        else
            prdElse.getActions().forEach(actToPerform -> ActionsPerformer.fireAction(world, actToPerform, on));
    }
    public static void handle(World world, Action action, SingleEntity on) {
        if (Objects.isNull(on))
            handleAll(world, action);

        else
            handleSingle(world, action, on);

        Loggers.SIMULATION_LOGGER.info("Condition evaluate");
    }
}