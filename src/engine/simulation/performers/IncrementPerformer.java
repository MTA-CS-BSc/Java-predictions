package engine.simulation.performers;

import engine.consts.ActionTypes;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;
import helpers.Constants;

import java.util.Objects;

public abstract class IncrementPerformer {
    private static String getIncrementResult(String propValue, String by) {
        String result = String.valueOf(Float.parseFloat(propValue) + Float.parseFloat(by));
        return result.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT) ? result.split("\\.")[0] : result;
    }
    private static void handleAll(World world, Action action) throws Exception {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        for (SingleEntity entity : mainEntity.getSingleEntities())
            handleSingle(world, action, entity);
    }
    private static void handleSingle(World world, Action action, SingleEntity on) throws Exception {
        Property propToChange = Utils.findPropertyByName(on, action.getPropertyName());
        String by = ExpressionParser.evaluateExpression(world, action, action.getBy(), on);
        String newValue = getIncrementResult(propToChange.getValue().getCurrentValue(), by);
        ActionsPerformer.setPropertyValue(ActionTypes.INCREASE, action.getEntityName(), propToChange, newValue);
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
