package simulation.performers;

import exceptions.ErrorMessageFormatter;
import exceptions.PropertyNotFoundException;
import modules.Utils;
import parsers.ExpressionParser;
import prototypes.prd.implemented.Property;
import prototypes.SingleEntity;
import prototypes.prd.implemented.World;
import prototypes.prd.implemented.actions.IncreaseAction;
import modules.Restrictions;
import types.ActionTypes;

import java.util.Objects;

public abstract class IncreasePerformer {
    private static String getIncrementResult(String propValue, String by) {
        String result = String.valueOf(Float.parseFloat(propValue) + Float.parseFloat(by));
        return result.matches(Restrictions.REGEX_ONLY_ZEROES_AFTER_DOT) ? result.split("\\.")[0] : result;
    }

    private static void handle(World world, IncreaseAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        Property propToChange = Utils.findPropertyByName(main, action.getPropertyName());
        String by = ExpressionParser.evaluateExpression(world, action.getBy(), main, secondary);
        String newValue = getIncrementResult(propToChange.getValue().getCurrentValue(), by);
        ActionsPerformer.setPropertyValue(ActionTypes.INCREASE, action.getEntityName(), propToChange, newValue);
    }

    public static void performAction(World world, IncreaseAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getPropertyName()));

        handle(world, action, main, secondary);
    }
}
