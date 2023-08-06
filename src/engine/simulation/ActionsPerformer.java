package engine.simulation;

import engine.consts.*;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.*;
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
                Loggers.SIMULATION_LOGGER.info(String.format("Can't change property [%s]" +
                                " value from [%s] to [%s] due to range restriction",
                        property.getName(), property.getValue().getCurrentValue(), newValue));

                return false;
            }
        }

        return true;
    }
    public void handleSetAction(World world, PRDAction action) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntity());
        String parsedValue = ExpressionParser.parseExpression(world, action, action.getValue());
        Property property = Utils.findPropertyByName(world, action.getEntity(), action.getProperty());

        if (Objects.isNull(mainEntity) || Objects.isNull(property))
            return;

        if (PropTypes.NUMERIC_PROPS.contains(property.getType()))
            if (!validateNewValueInRange(property, parsedValue))
                return;

        mainEntity.getSingleEntities().forEach(entity -> {
            String newValue = ExpressionParser.evaluateExpression(parsedValue, entity);

            Property propToChange = entity.getProperties().getPropsMap().get(property.getName());

            Loggers.SIMULATION_LOGGER.info(String.format("Changing property [%s]" +
                    "value [%s]->[%s]", property.getName(), propToChange.getValue().getCurrentValue(), newValue));

            propToChange.getValue().setCurrentValue(newValue);
            propToChange.setStableTime(0);

        });
    }
    private String getNewValueForIncrementDecrement(PRDAction action,
                                                    Property property, String by) {
        if (action.getType().equals(ActionTypes.INCREASE))
            return String.valueOf(Float.parseFloat(property.getValue().getCurrentValue()) + Float.parseFloat(by));

        else if (action.getType().equals(ActionTypes.DECREASE))
            return String.valueOf(Float.parseFloat(property.getValue().getCurrentValue()) - Float.parseFloat(by));

        return "";
    }
    public void handleIncrementDecrementAction(World world, PRDAction action) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntity());
        String parsedValue = ExpressionParser.parseExpression(world, action, action.getBy());
        Property property = Utils.findPropertyByName(world, action.getEntity(), action.getProperty());

        if (Objects.isNull(mainEntity) || Objects.isNull(property))
            return;

        mainEntity.getSingleEntities().forEach(entity -> {
            Property propToChange = entity.getProperties().getPropsMap().get(property.getName());
            String newValue = getNewValueForIncrementDecrement(action, propToChange, ExpressionParser.evaluateExpression(parsedValue, entity));

            if (validateNewValueInRange(property, newValue)) {
                Loggers.SIMULATION_LOGGER.info(String.format("Changing property [%s]" +
                        "value [%s]->[%s]", property.getName(), propToChange.getValue().getCurrentValue(), newValue));

                propToChange.getValue().setCurrentValue(newValue);
                propToChange.setStableTime(0);
            }
        });
    }
    private String getCalculationResult(World world, PRDAction action, SingleEntity singleEntity) {
        PRDMultiply multiply = action.getPRDMultiply();
        PRDDivide divide = action.getPRDDivide();

        String arg1 = ExpressionParser.parseExpression(world, action, Objects.isNull(multiply) ? divide.getArg1() : multiply.getArg1());
        String arg2 = ExpressionParser.parseExpression(world, action, Objects.isNull(multiply) ? divide.getArg2() : multiply.getArg2());

        if (arg1.isEmpty() || arg2.isEmpty())
            return "";
        
        String eval_arg1 = ExpressionParser.evaluateExpression(arg1, singleEntity);
        String eval_arg2 = ExpressionParser.evaluateExpression(arg2, singleEntity);

        return String.valueOf(Objects.isNull(multiply) ? Float.parseFloat(eval_arg1) / Float.parseFloat(eval_arg2)
                : Float.parseFloat(eval_arg1) * Float.parseFloat(eval_arg2));
    }
    public void handleCalculationAction(World world, PRDAction action) {
        Property someResultProp = Utils.findPropertyByName(world, action.getEntity(), action.getResultProp());
        Entity mainEntity = Utils.findEntityByName(world, action.getEntity());

        if (Objects.isNull(mainEntity) || Objects.isNull(someResultProp)) {
            Loggers.SIMULATION_LOGGER.info(String.format("In calculation action," +
                    " entity [%s] or result prop [%s] not found", action.getEntity(), action.getResultProp()));
            return;
        }

        mainEntity.getSingleEntities().forEach(entity -> {
            String calculationResult = getCalculationResult(world, action, entity);
            Property resultProp = Utils.findPropertyByName(entity, action.getResultProp());

            if (validateNewValueInRange(resultProp, calculationResult)) {
                Loggers.SIMULATION_LOGGER.info(String.format("Setting [%s] on entity [%s] = [%s]",
                                resultProp.getName(), action.getEntity(), calculationResult));

                resultProp.getValue().setCurrentValue(calculationResult);
                resultProp.setStableTime(0);
            }
        });

    }

}
