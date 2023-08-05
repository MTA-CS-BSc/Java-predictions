package engine.simulation;

import engine.logs.Loggers;
import engine.modules.*;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActionsPerformer {
    public void fireAction(World world, PRDAction action) {
        String type = action.getType();

        if (type.equalsIgnoreCase(ActionTypes.INCREASE)
                || type.equalsIgnoreCase(ActionTypes.DECREASE))
            handleIncrementDecrementAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.CALCULATION))
            handleCalculationAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.SET))
            handleSetAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.KILL))
            handleKillAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.CONDITION))
            handleConditionAction(world, action);
    }
    public boolean validateNewValueInRange(Property property, String newValue) {
        if (!Objects.isNull(property.getRange())) {
            if (Float.parseFloat(newValue) > property.getRange().getTo()
                    || Float.parseFloat(newValue) < property.getRange().getFrom()) {
                Loggers.SIMULATION_LOGGER.info("Can't perform increment/decrement due to range issue");
                return false;
            }

        }

        return true;
    }
    private String getNewValueForIncrementDecrement(PRDAction action,
                                                   Property property, String by) {
        if (action.getType().equals(ActionTypes.INCREASE))
            return String.valueOf(Float.parseFloat(property.getValue().getCurrentValue()) + Float.parseFloat(by));

        else if (action.getType().equals(ActionTypes.DECREASE))
            return String.valueOf(Float.parseFloat(property.getValue().getCurrentValue()) - Float.parseFloat(by));

        return "";
    }
    private String getCalculationResult(World world, PRDAction action) {
        PRDMultiply multiply = action.getPRDMultiply();
        PRDDivide divide = action.getPRDDivide();

        String arg1 = Objects.isNull(multiply) ? divide.getArg1() : multiply.getArg1();
        String arg2 = Objects.isNull(multiply) ? divide.getArg2() : multiply.getArg2();

        String parsedArg1 = ExpressionParser.evaluateExpression(world, action, arg1);
        String parsedArg2 = ExpressionParser.evaluateExpression(world, action, arg2);

        if (parsedArg1.isEmpty() || parsedArg2.isEmpty())
            return "";

        return String.valueOf(Objects.isNull(multiply) ? Float.parseFloat(parsedArg1) / Float.parseFloat(parsedArg2)
                : Float.parseFloat(parsedArg1) * Float.parseFloat(parsedArg2));
    }
    private boolean evaluateSingleCondition(World world, PRDAction action, PRDCondition condition) {
        Property property = (Property)Utils.findPropertyByName(world, condition.getEntity(), condition.getProperty());
        String value = ExpressionParser.evaluateExpression(world, action, condition.getValue());
        String operator = condition.getOperator();
        boolean isNumeric = Utils.isDecimal(value);

        switch (operator) {
            case Operators.BT:
                if (isNumeric)
                    return Float.parseFloat(property.getValue().getCurrentValue()) > Float.parseFloat(value);
                break;
            case Operators.LT:
                if (isNumeric)
                    return Float.parseFloat(property.getValue().getCurrentValue()) < Float.parseFloat(value);
                break;
            case Operators.EQUALS:
                return value.equals(property.getValue().getCurrentValue());
            case Operators.NOT_EQUALS:
                return !value.equals(property.getValue().getCurrentValue());
        }

        return true;
    }
    private boolean evaluateMultipleCondition(World world, PRDAction action, PRDCondition condition) {
        List<PRDCondition> allConditions = condition.getPRDCondition();
        List<Boolean> allConditionsResults = new ArrayList<>();
        String logicalOperator = condition.getLogical();

        allConditions.forEach(current -> allConditionsResults.add(evaluateCondition(world, action, current)));

        return allConditionsResults
                .stream()
                .reduce(allConditionsResults.get(0), (item1, item2) ->
                        logicalOperator.equals(ConditionLogicalOperators.OR) ? item1 || item2 : item1 && item2);
    }
    private boolean evaluateCondition(World world, PRDAction action, PRDCondition condition) {
        if (condition.getSingularity().equals(ConditionSingularities.SINGLE))
            return evaluateSingleCondition(world, action, condition);

        return evaluateMultipleCondition(world, action, condition);
    }
    public void handleCalculationAction(World world, PRDAction action) {
        Property resultProp = (Property)Utils.findPropertyByName(world, action.getEntity(), action.getResultProp());

        if (Objects.isNull(resultProp))
            return;

        resultProp.setStableTime(0);
        String calculationResult = "";

        calculationResult = getCalculationResult(world, action);

        if (validateNewValueInRange(resultProp, calculationResult)) {
            Loggers.SIMULATION_LOGGER.info(String.format("Setting [%s] on entity [%s] = [%s]",
                    resultProp.getName(), action.getEntity(), calculationResult));

            resultProp.getValue().setCurrentValue(calculationResult);
        }

    }
    public void handleConditionAction(World world, PRDAction action) {
        PRDCondition condition = action.getPRDCondition();
        List<PRDAction> thenActions = action.getPRDThen().getPRDAction();
        PRDElse prdElse = action.getPRDElse();

        boolean conditionResult = evaluateCondition(world, action, condition);

        if (conditionResult)
            thenActions.forEach(actToPerform -> fireAction(world, actToPerform));

        else if (!Objects.isNull(prdElse))
            prdElse.getPRDAction().forEach(actToPerform -> fireAction(world, actToPerform));
    }
    public void handleIncrementDecrementAction(World world, PRDAction action) {
        Property property = (Property)Utils.findPropertyByName(world, action.getEntity(), action.getProperty());
        String by = ExpressionParser.evaluateExpression(world, action, action.getBy());

        if (by.isEmpty() || Objects.isNull(property))
            return;

        property.setStableTime(0);
        String newValue = getNewValueForIncrementDecrement(action, property, by);

        if (validateNewValueInRange(property, newValue)) {
            Loggers.SIMULATION_LOGGER.info(String.format("Changing property [%s]" +
                    " value from [%s] to [%s]", property.getName(), property.getValue().getCurrentValue(), newValue));

            property.getValue().setCurrentValue(newValue);
        }
    }
    public void handleKillAction(World world, PRDAction action) {
        Loggers.SIMULATION_LOGGER.info(String.format("Killing entity [%s]", action.getEntity()));
        world.getEntities().getEntitiesMap().remove(action.getEntity());
    }
    public void handleSetAction(World world, PRDAction action) {
        Property property = (Property)Utils.findPropertyByName(world, action.getEntity(), action.getProperty());
        String value = ExpressionParser.evaluateExpression(world, action, action.getValue());

        if (Objects.isNull(value) || Objects.isNull(property))
            return;

        property.setStableTime(0);

        if (PropTypes.NUMERIC_PROPS.contains(property.getType()))
            if (!validateNewValueInRange(property, value))
                return;

        Loggers.SIMULATION_LOGGER.info(String.format("Changing property [%s]" +
                "value [%s]->[%s]", property.getName(), property.getValue().getCurrentValue(), value));

        property.getValue().setCurrentValue(value);
    }
}
