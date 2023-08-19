package engine.simulation.performers;

import engine.consts.ActionTypes;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.Objects;

public abstract class SetPerformer {
    private static void handleAll(World world, Action action) throws Exception {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        for (SingleEntity entity : mainEntity.getSingleEntities())
            handleSingle(world, action, entity);
    }
    private static void handleSingle(World world, Action action, SingleEntity on) throws Exception {
        Property property = Utils.findPropertyByName(on, action.getPropertyName());
        String newValue = ExpressionParser.evaluateExpression(world, action, action.getValue(), on);
        ActionsPerformer.setPropertyValue(ActionTypes.SET, action.getEntityName(), property, newValue);
    }
    private static void performAction(World world, Action action, SingleEntity on) throws Exception {
        if (Objects.isNull(on))
            handleAll(world, action);

        else
            handleSingle(world, action, on);
    }
    public static void handle(World world, Action action, SingleEntity on) throws Exception {
        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getPropertyName()));

        performAction(world, action, on);
    }
}
