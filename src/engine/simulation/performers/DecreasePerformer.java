package engine.simulation.performers;

import helpers.ActionTypes;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;
import engine.prototypes.implemented.actions.DecreaseAction;
import helpers.Constants;

import java.util.Objects;

public abstract class DecreasePerformer {
    private static String getDecrementResult(String propValue, String by) {
        String result = String.valueOf(Float.parseFloat(propValue) - Float.parseFloat(by));

        return result.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT) ? result.split("\\.")[0] : result;
    }
    private static void handle(World world, DecreaseAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        Property propToChange = Utils.findPropertyByName(main, action.getPropertyName());
        String by = ExpressionParser.evaluateExpression(world, action.getBy(), main, secondary);
        String newValue = getDecrementResult(propToChange.getValue().getCurrentValue(), by);
        ActionsPerformer.setPropertyValue(ActionTypes.DECREASE, action.getEntityName(), propToChange, newValue);
    }
    public static void performAction(World world, DecreaseAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getPropertyName()));

        handle(world, action, main, secondary);
    }
}
