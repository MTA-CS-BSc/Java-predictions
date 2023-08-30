package engine.simulation.performers;

import engine.consts.ActionTypes;
import engine.exceptions.*;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.CalculationAction;
import helpers.Constants;

import java.util.Objects;

public abstract class CalculationPerformer {
    private static String getCalculationResult(World world, CalculationAction action, SingleEntity on) throws Exception {
        Multiply multiply = action.getMultiply();
        Divide divide = action.getDivide();

        String arg1 = ExpressionParser.evaluateExpression(world, action, Objects.isNull(multiply) ? divide.getArg1() : multiply.getArg1(), on);
        String arg2 = ExpressionParser.evaluateExpression(world, action, Objects.isNull(multiply) ? divide.getArg2() : multiply.getArg2(), on);

        if (arg1.isEmpty() || arg2.isEmpty())
            throw new EmptyExpressionException(String.format("Action [%s]: Type [%s]: Arg1 or Arg2 are not valid expressions",
                    action.getType(), Objects.isNull(multiply) ? "Divide" : "Multiply"));

        if (!Objects.isNull(divide) && Float.parseFloat(arg2) == 0)
            throw new InvalidTypeException("Action [%s]: Can't divide by zero!");

        String result = String.valueOf(Objects.isNull(multiply) ? Float.parseFloat(arg1) / Float.parseFloat(arg2)
                : Float.parseFloat(arg1) * Float.parseFloat(arg2));

        return result.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT) ? result.split("\\.")[0] : result;

    }
    private static void handleAll(World world, CalculationAction action) throws Exception {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        for (SingleEntity entity : mainEntity.getSingleEntities())
            handleSingle(world, action, entity);
    }
    private static void handleSingle(World world, CalculationAction action, SingleEntity on) throws Exception {
        Property resultProperty = Utils.findPropertyByName(on, action.getResultPropertyName());
        String newValue = getCalculationResult(world, action, on);
        ActionsPerformer.setPropertyValue(ActionTypes.CALCULATION, action.getEntityName(), resultProperty, newValue);
    }
    private static void performAction(World world, CalculationAction action, SingleEntity on) throws Exception {
        if (Objects.isNull(on))
            handleAll(world, action);

        else
            handleSingle(world, action, on);
    }
    public static void handle(World world, CalculationAction action, SingleEntity on) throws Exception {
        if (Objects.isNull(Utils.findEntityByName(world, action.getEntityName())))
            throw new EntityNotFoundException(String.format("Action [%s]: Entity [%s] does not exist", action.getType(), action.getEntityName()));

        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getResultPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getResultPropertyName()));

        performAction(world, action, on);
    }
}
