package engine.simulation.performers;

import engine.consts.ActionTypes;
import engine.exceptions.*;
import engine.logs.EngineLoggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.Objects;

public abstract class CalculationPerformer {
    private static String getCalculationResult(World world, Action action, SingleEntity on) throws Exception {
        Multiply multiply = action.getMultiply();
        Divide divide = action.getDivide();

        String arg1 = ExpressionParser.evaluateExpression(world, action, Objects.isNull(multiply) ? divide.getArg1() : multiply.getArg1(), on);
        String arg2 = ExpressionParser.evaluateExpression(world, action, Objects.isNull(multiply) ? divide.getArg2() : multiply.getArg2(), on);

        if (arg1.isEmpty() || arg2.isEmpty())
            throw new EmptyExpressionException(String.format("Action [%s]: Type [%s]: Arg1 or Arg2 are not valid expressions",
                    action.getType(), Objects.isNull(multiply) ? "Divide" : "Multiply"));

        String result = String.valueOf(Objects.isNull(multiply) ? Float.parseFloat(arg1) / Float.parseFloat(arg2)
                : Float.parseFloat(arg1) * Float.parseFloat(arg2));

        return result.matches(Utils.REGEX_ONLY_ZEROES_AFTER_DOT) ? result.split("\\.")[0] : result;

    }
    private static void handleAll(World world, Action action) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        mainEntity.getSingleEntities().forEach(entity -> {
            try {
                handleSingle(world, action, entity);
            } catch (Exception e) {
                EngineLoggers.SIMULATION_LOGGER.info(e.getMessage());
            }
        });
    }
    private static void handleSingle(World world, Action action, SingleEntity on) throws Exception {
        Property resultProperty = Utils.findPropertyByName(on, action.getResultPropertyName());
        String newValue = getCalculationResult(world, action, on);
        ActionsPerformer.setPropertyValue(ActionTypes.CALCULATION, action.getEntityName(), resultProperty, newValue);
    }
    private static void performAction(World world, Action action, SingleEntity on) {
        if (Objects.isNull(on))
            handleAll(world, action);

        else {
            try {
                handleSingle(world, action, on);
            } catch (Exception e) {
                EngineLoggers.SIMULATION_LOGGER.info(e.getMessage());
            }
        }
    }
    public static void handle(World world, Action action, SingleEntity on) throws PropertyNotFoundException {
        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getResultPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getPropertyName()));

        performAction(world, action, on);
    }
}
