package engine.simulation;

import engine.logs.Loggers;
import engine.modules.ActionTypes;
import engine.modules.PropTypes;
import engine.parsers.ExpressionParser;
import engine.modules.Utils;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDDivide;
import engine.prototypes.jaxb.PRDMultiply;

import java.util.Objects;

public class ActionsPerformer {

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
        // TODO: Implement
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
