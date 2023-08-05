package engine.simulation;

import engine.logs.Loggers;
import engine.modules.ActionTypes;
import engine.modules.ExpressionParser;
import engine.modules.Utils;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDAction;

import java.util.Objects;

public class ActionsPerformer {
    public String getNewValueForIncrementDecrement(PRDAction action,
                                                   Property property, Object by) {
        String newValue = "";

        if (by.getClass() == Float.class || by.getClass() == Integer.class) {
            if (action.getType().equals(ActionTypes.INCREASE))
                newValue = String.valueOf(Float.parseFloat(property.getValue().getCurrentValue()) + (float)by);

            else
                newValue = String.valueOf(Float.parseFloat(property.getValue().getCurrentValue()) - (float)by);
        }

        if (by.getClass() == Property.class) {
            if (action.getType().equals(ActionTypes.INCREASE))
                newValue = String.valueOf(Float.parseFloat(property.getValue().getCurrentValue())
                        + Float.parseFloat(((Property) by).getValue().getCurrentValue()));

            else
                newValue = String.valueOf(Float.parseFloat(property.getValue().getCurrentValue())
                        - Float.parseFloat(((Property) by).getValue().getCurrentValue()));
        }

        return newValue;
    }
    public void handleIncrementDecrementAction(World world, PRDAction action) {
        Property property = (Property)Utils.findPropertyByName(world, action.getEntity(), action.getProperty());
        Object by = ExpressionParser.parseExpression(world, action, action.getBy());

        if (Objects.isNull(by) || Objects.isNull(property))
            return;

        property.setStableTime(0);
        property.getValue().setCurrentValue(getNewValueForIncrementDecrement(action, property, by));
    }
    public void handleKillAction(World world, PRDAction action) {
        Loggers.SIMULATION_LOGGER.info(String.format("Killing entity [%s]", action.getEntity()));
        world.getEntities().getEntitiesMap().remove(action.getEntity());
    }
}
