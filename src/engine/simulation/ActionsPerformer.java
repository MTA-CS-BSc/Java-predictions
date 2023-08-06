package engine.simulation;

import engine.consts.*;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.*;

public class ActionsPerformer {
    public void fireAction(World world, Action action, SingleEntity on) {
        String type = action.getType();

        if (type.equalsIgnoreCase(ActionTypes.INCREASE)
                || type.equalsIgnoreCase(ActionTypes.DECREASE))
            handleIncrementDecrementAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.CALCULATION))
            handleCalculationAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.SET))
            handleSetAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.KILL))
            handleKillAction(world, action, on);

        else if (type.equalsIgnoreCase(ActionTypes.CONDITION))
            handleConditionAction(world, action);
    }
    public boolean validateNewValueInRange(Property property, String newValue) {
        if (!Objects.isNull(property.getRange())) {
            if (Float.parseFloat(newValue) > property.getRange().getTo()
                    || Float.parseFloat(newValue) < property.getRange().getFrom()) {
                Loggers.SIMULATION_LOGGER.info(String.format("Can't change property [%s]" +
                                " value from [%s] to [%s] due to range restriction",
                        property.getName(), property.getValue().getCurrentValue(), newValue));

                return false;
            }
        }

        return true;
    }
    public void handleSetAction(World world, Action action) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());
        String parsedValue = ExpressionParser.parseExpression(world, action, action.getValue());
        Property property = Utils.findPropertyByName(world, action.getEntityName(), action.getPropertyName());

        if (Objects.isNull(mainEntity) || Objects.isNull(property))
            return;

        if (PropTypes.NUMERIC_PROPS.contains(property.getType()))
            if (!validateNewValueInRange(property, parsedValue))
                return;

        mainEntity.getSingleEntities().forEach(entity -> {
            String newValue = ExpressionParser.evaluateExpression(parsedValue, entity);

            Property propToChange = entity.getProperties().getPropsMap().get(property.getName());

            Loggers.SIMULATION_LOGGER.info(String.format("Changing property [%s]" +
                    " value [%s]->[%s]", property.getName(), propToChange.getValue().getCurrentValue(), newValue));

            propToChange.getValue().setCurrentValue(newValue);
            propToChange.setStableTime(0);

        });
    }
    private String getNewValueForIncrementDecrement(Action action,
                                                    Property property, String by) {
        if (action.getType().equals(ActionTypes.INCREASE))
            return String.valueOf(Float.parseFloat(property.getValue().getCurrentValue()) + Float.parseFloat(by));

        else if (action.getType().equals(ActionTypes.DECREASE))
            return String.valueOf(Float.parseFloat(property.getValue().getCurrentValue()) - Float.parseFloat(by));

        return "";
    }
    public void handleIncrementDecrementAction(World world, Action action) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());
        String parsedValue = ExpressionParser.parseExpression(world, action, action.getBy());
        Property property = Utils.findPropertyByName(world, action.getEntityName(), action.getPropertyName());

        if (Objects.isNull(mainEntity) || Objects.isNull(property))
            return;

        mainEntity.getSingleEntities().forEach(entity -> {
            Property propToChange = entity.getProperties().getPropsMap().get(property.getName());
            String newValue = getNewValueForIncrementDecrement(action, propToChange, ExpressionParser.evaluateExpression(parsedValue, entity));

            if (validateNewValueInRange(property, newValue)) {
                Loggers.SIMULATION_LOGGER.info(String.format("Changing property [%s]" +
                        " value [%s]->[%s]", property.getName(), propToChange.getValue().getCurrentValue(), newValue));

                propToChange.getValue().setCurrentValue(newValue);
                propToChange.setStableTime(0);
            }
        });
    }
    private String getCalculationResult(World world, Action action, SingleEntity singleEntity) {
        Multiply multiply = action.getMultiply();
        Divide divide = action.getDivide();

        String arg1 = ExpressionParser.parseExpression(world, action, Objects.isNull(multiply) ? divide.getArg1() : multiply.getArg1());
        String arg2 = ExpressionParser.parseExpression(world, action, Objects.isNull(multiply) ? divide.getArg2() : multiply.getArg2());

        if (arg1.isEmpty() || arg2.isEmpty())
            return "";
        
        String eval_arg1 = ExpressionParser.evaluateExpression(arg1, singleEntity);
        String eval_arg2 = ExpressionParser.evaluateExpression(arg2, singleEntity);

        return String.valueOf(Objects.isNull(multiply) ? Float.parseFloat(eval_arg1) / Float.parseFloat(eval_arg2)
                : Float.parseFloat(eval_arg1) * Float.parseFloat(eval_arg2));
    }
    public void handleCalculationAction(World world, Action action) {
        Property someResultProp = Utils.findPropertyByName(world, action.getEntityName(), action.getResultPropertyName());
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        if (Objects.isNull(mainEntity) || Objects.isNull(someResultProp)) {
            Loggers.SIMULATION_LOGGER.info(String.format("In calculation action," +
                    " entity [%s] or result prop [%s] not found", action.getEntityName(), action.getResultPropertyName()));
            return;
        }

        mainEntity.getSingleEntities().forEach(entity -> {
            String calculationResult = getCalculationResult(world, action, entity);
            Property resultProp = Utils.findPropertyByName(entity, action.getResultPropertyName());

            if (validateNewValueInRange(resultProp, calculationResult)) {
                Loggers.SIMULATION_LOGGER.info(String.format("Setting [%s] on entity [%s] = [%s]",
                                resultProp.getName(), action.getEntityName(), calculationResult));

                resultProp.getValue().setCurrentValue(calculationResult);
                resultProp.setStableTime(0);
            }
        });

    }
    private boolean evaluateMultipleCondition(World world, Action action, Condition condition, SingleEntity singleEntity) {
        List<Condition> allConditions = condition.getConditions();
        List<Boolean> allConditionsResults = new ArrayList<>();
        String logicalOperator = condition.getLogicalOperator();

        allConditions.forEach(current -> allConditionsResults.add(evaluateCondition(world, action, current, singleEntity)));

        return allConditionsResults
                .stream()
                .reduce(allConditionsResults.get(0), (item1, item2) ->
                        logicalOperator.equals(ConditionLogicalOperators.OR) ? item1 || item2 : item1 && item2);
    }
    private boolean evaluateSingleCondition(World world, Action action, Condition condition, SingleEntity singleEntity) {
        Property property = singleEntity.getProperties().getPropsMap().get(condition.getProperty());

        if (Objects.isNull(Utils.findEntityByName(world, condition.getEntityName()))
                || Objects.isNull(property) || Objects.isNull(property.getValue())) {
            Loggers.SIMULATION_LOGGER.info(String.format("Entity [%s] or property [%s] do not exist",
                    condition.getEntityName(), condition.getProperty()));
            return true;
        }

        String parsedValue = ExpressionParser.parseExpression(world, action, condition.getValue());
        String value = ExpressionParser.evaluateExpression(parsedValue, singleEntity);
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
    private boolean evaluateCondition(World world, Action action, Condition condition, SingleEntity singleEntity) {
        if (condition.getSingularity().equals(ConditionSingularities.SINGLE))
            return evaluateSingleCondition(world, action, condition, singleEntity);

        return evaluateMultipleCondition(world, action, condition, singleEntity);
    }
    public void handleConditionAction(World world, Action action) {
        Condition condition = action.getCondition();
        List<Action> thenActions = action.getThen().getActions();
        Else prdElse = action.getElse();

        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        if (Objects.isNull(mainEntity))
            return;

        mainEntity.getSingleEntities().forEach(singleEntity -> {
           boolean conditionResult = evaluateCondition(world, action, condition, singleEntity);

           if (conditionResult)
               thenActions.forEach(actToPerform -> fireAction(world, actToPerform, singleEntity));

           else
               prdElse.getActions().forEach(actToPerform -> fireAction(world, actToPerform, singleEntity));
        });
    }
    public void handleKillAllSingleEntities(World world, Action action) {
        Loggers.SIMULATION_LOGGER.info(String.format("Killing entity [%s]", action.getEntityName()));
        world.getEntities().getEntitiesMap().remove(action.getEntityName());
    }
    public void handleKillAction(World world, Action action, SingleEntity entityToKill) {
        if (Objects.isNull(entityToKill))
            handleKillAllSingleEntities(world, action);

        else {
            Loggers.SIMULATION_LOGGER.info(String.format("Killing 1 entity of entity [%s]", action.getEntityName()));
            Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

            mainEntity.setPopulation(mainEntity.getPopulation() - 1);
            List<SingleEntity> updatedList = new ArrayList<>();

            mainEntity.getSingleEntities().forEach(singleEntity -> {
               if (!singleEntity.equals(entityToKill))
                   updatedList.add(singleEntity);
            });

            mainEntity.setSingleEntities(updatedList);
        }
    }
}
