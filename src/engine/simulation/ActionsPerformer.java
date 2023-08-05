package engine.simulation;

import engine.logs.Loggers;
import engine.modules.ActionTypes;
import engine.parsers.ExpressionParser;
import engine.modules.Utils;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDDivide;
import engine.prototypes.jaxb.PRDMultiply;

import java.util.Objects;

public class ActionsPerformer {
    public String getNewValueForIncrementDecrement(PRDAction action,
                                                   Property property, Object by) {
        String newValue = "";
        boolean isDecimal = Utils.isDecimal(by.toString());

        if (by.getClass().isPrimitive() || isDecimal) {
            if (action.getType().equals(ActionTypes.INCREASE))
                newValue = String.valueOf(Float.parseFloat(property.getValue().getCurrentValue()) + Float.parseFloat(by.toString()));

            else
                newValue = String.valueOf(Float.parseFloat(property.getValue().getCurrentValue()) - Float.parseFloat(by.toString()));
        }

        else if (by.getClass() == Property.class) {
            if (action.getType().equals(ActionTypes.INCREASE))
                newValue = String.valueOf(Float.parseFloat(property.getValue().getCurrentValue())
                        + Float.parseFloat(((Property) by).getValue().getCurrentValue()));

            else
                newValue = String.valueOf(Float.parseFloat(property.getValue().getCurrentValue())
                        - Float.parseFloat(((Property) by).getValue().getCurrentValue()));
        }

        return newValue;
    }
    public String getNewValueForSet(PRDAction action, Object value) {
        String newValue = "";

        if (value.getClass().isPrimitive())
            newValue = value.toString();

        else if (value.getClass() == Property.class)
            newValue = ((Property)value).getValue().getCurrentValue();

        else
            newValue = ((String)value);

        return newValue;
    }
    public void handleIncrementDecrementAction(World world, PRDAction action) {
        Property property = (Property)Utils.findPropertyByName(world, action.getEntity(), action.getProperty());
        Object by = ExpressionParser.parseExpression(world, action, action.getBy());

        if (Objects.isNull(by) || Objects.isNull(property))
            return;

        property.setStableTime(0);
        String newValue = getNewValueForIncrementDecrement(action, property, by);

        Loggers.SIMULATION_LOGGER.info(String.format("Changing property [%s]" +
                "value [%s]->[%s]", property.getName(), property.getValue().getCurrentValue(), newValue));

        property.getValue().setCurrentValue(newValue);
    }
    public void handleKillAction(World world, PRDAction action) {
        Loggers.SIMULATION_LOGGER.info(String.format("Killing entity [%s]", action.getEntity()));
        world.getEntities().getEntitiesMap().remove(action.getEntity());
    }
    public void handleSetAction(World world, PRDAction action) {
        Property property = (Property)Utils.findPropertyByName(world, action.getEntity(), action.getProperty());
        Object value = ExpressionParser.parseExpression(world, action, action.getValue());

        if (Objects.isNull(value) || Objects.isNull(property))
            return;

        property.setStableTime(0);
        String newValue = getNewValueForSet(action, value);

        Loggers.SIMULATION_LOGGER.info(String.format("Changing property [%s]" +
                "value [%s]->[%s]", property.getName(), property.getValue().getCurrentValue(), newValue));

        property.getValue().setCurrentValue(newValue);
    }
    public String getCalculationResult(World world, PRDAction action) {
        PRDMultiply multiply = action.getPRDMultiply();
        PRDDivide divide = action.getPRDDivide();

        String arg1 = Objects.isNull(multiply) ? divide.getArg1() : multiply.getArg1();
        String arg2 = Objects.isNull(multiply) ? divide.getArg2() : multiply.getArg2();

        Object parsedArg1 = ExpressionParser.parseExpression(world, action, arg1);
        Object parsedArg2 = ExpressionParser.parseExpression(world, action, arg1);

        if (Objects.isNull(parsedArg1) || Objects.isNull(parsedArg2))
            return "";

        float arg1_float = 0.0f;
        float arg2_float = 1.0f;

        if (parsedArg1.getClass() == Property.class)
            arg1_float = Float.parseFloat(((Property) parsedArg1).getValue().getCurrentValue());

        else if (parsedArg1.getClass().isPrimitive())
            arg1_float = (float)parsedArg1;

        if (parsedArg2.getClass() == Property.class)
            arg2_float = Float.parseFloat(((Property) parsedArg2).getValue().getCurrentValue());

        else if (parsedArg2.getClass().isPrimitive())
            arg2_float = (float)parsedArg2;

        return Objects.isNull(multiply) ? String.valueOf(arg1_float / arg2_float)
                : String.valueOf(arg1_float * arg2_float);
    }
    public void handleCalculationAction(World world, PRDAction action) {
        Property resultProp = (Property)Utils.findPropertyByName(world, action.getEntity(), action.getResultProp());

        if (Objects.isNull(resultProp))
            return;

        resultProp.setStableTime(0);
        String calculationResult = "";

        calculationResult = getCalculationResult(world, action);

        Loggers.SIMULATION_LOGGER.info(String.format("Changing prop [%s] on entity [%s], [%s]->[%s]",
                action.getResultProp(), action.getEntity(),
                resultProp.getValue().getCurrentValue(),
                calculationResult));

        resultProp.getValue().setCurrentValue(calculationResult);

    }
}
