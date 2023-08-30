package engine.simulation.performers;

import engine.consts.ActionTypes;
import engine.exceptions.EntityNotFoundException;
import engine.exceptions.ErrorMessageFormatter;
import engine.exceptions.PropertyNotFoundException;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.DecreaseAction;
import helpers.Constants;

import java.util.Objects;

public abstract class DecreasePerformer {
    private static String getDecrementResult(String propValue, String by) {
        String result = String.valueOf(Float.parseFloat(propValue) - Float.parseFloat(by));

        return result.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT) ? result.split("\\.")[0] : result;
    }
    private static void handleAll(World world, DecreaseAction action) throws Exception {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        for (SingleEntity entity : mainEntity.getSingleEntities())
            handleSingle(world, action, entity);
    }
    private static void handleSingle(World world, DecreaseAction action, SingleEntity on) throws Exception {
        Property propToChange = Utils.findPropertyByName(on, action.getPropertyName());
        String by = ExpressionParser.evaluateExpression(world, action, action.getBy(), on);
        String newValue = getDecrementResult(propToChange.getValue().getCurrentValue(), by);
        ActionsPerformer.setPropertyValue(ActionTypes.DECREASE, action.getEntityName(), propToChange, newValue);
    }
    private static void performAction(World world, DecreaseAction action, SingleEntity on) throws Exception {
        if (Objects.isNull(on))
            handleAll(world, action);

        else
            handleSingle(world, action, on);
    }
    public static void handle(World world, DecreaseAction action, SingleEntity on) throws Exception {
        if (Objects.isNull(Utils.findEntityByName(world, action.getEntityName())))
            throw new EntityNotFoundException(String.format("Action [%s]: Entity [%s] does not exist", action.getType(), action.getEntityName()));

        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getPropertyName()));

        performAction(world, action, on);
    }
}
