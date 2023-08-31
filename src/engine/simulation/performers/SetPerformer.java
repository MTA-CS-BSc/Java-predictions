package engine.simulation.performers;

import engine.consts.ActionTypes;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;
import engine.prototypes.implemented.actions.SetAction;

import java.util.Objects;

public abstract class SetPerformer {
    private static void handle(World world, SetAction action, SingleEntity main) throws Exception {
        Property property = Utils.findPropertyByName(main, action.getPropertyName());
        String newValue = ExpressionParser.evaluateExpression(world, action.getValue(), main);
        ActionsPerformer.setPropertyValue(ActionTypes.SET, action.getEntityName(), property, newValue);
    }
    public static void performAction(World world, SetAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getPropertyName()));

        handle(world, action, action.getEntityName().equals(main.getEntityName()) ? main : secondary);
    }
}
