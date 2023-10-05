package simulation.performers;

import exceptions.ErrorMessageFormatter;
import exceptions.PropertyNotFoundException;
import modules.Utils;
import parsers.ExpressionParser;
import prototypes.prd.implemented.Property;
import prototypes.SingleEntity;
import prototypes.prd.implemented.World;
import prototypes.prd.implemented.actions.SetAction;
import types.ActionTypes;

import java.util.Objects;

public abstract class SetPerformer {
    private static void handle(World world, SetAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        Property property = Utils.findPropertyByName(main, action.getPropertyName());
        String newValue = ExpressionParser.evaluateExpression(world, action.getValue(), main, secondary);
        ActionsPerformer.setPropertyValue(ActionTypes.SET, action.getEntityName(), property, newValue);
    }

    public static void performAction(World world, SetAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getPropertyName()));

        handle(world, action, main, secondary);
    }
}
