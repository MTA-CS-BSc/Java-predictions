package engine.simulation.performers;

import engine.exceptions.*;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.Objects;

import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.Objects;
public class CalculationPerformer {
    private static String getCalculationResult(World world, Action action, SingleEntity on) throws EmptyExpressionException {
        Multiply multiply = action.getMultiply();
        Divide divide = action.getDivide();

        String arg1 = ExpressionParser.parseExpression(world, action, Objects.isNull(multiply) ? divide.getArg1() : multiply.getArg1());
        String arg2 = ExpressionParser.parseExpression(world, action, Objects.isNull(multiply) ? divide.getArg2() : multiply.getArg2());

        if (arg1.isEmpty() || arg2.isEmpty())
            throw new EmptyExpressionException(String.format("Action [%s]: Type [%s]: Arg1 or Arg2 are not valid expressions",
                    action.getType(), Objects.isNull(multiply) ? "Divide" : "Multiply"));

        String eval_arg1 = ExpressionParser.evaluateExpression(arg1, on);
        String eval_arg2 = ExpressionParser.evaluateExpression(arg2, on);

        return String.valueOf(Objects.isNull(multiply) ? Float.parseFloat(eval_arg1) / Float.parseFloat(eval_arg2)
                : Float.parseFloat(eval_arg1) * Float.parseFloat(eval_arg2));
    }
    private static void handleAll(World world, Entity mainEntity, Action action) {
        mainEntity.getSingleEntities().forEach(entity -> {
            try {
                handleSingle(world, action, entity);
            } catch (ValueNotInRangeException | EmptyExpressionException e) {
                Loggers.SIMULATION_LOGGER.info(e.getMessage());
            }
        });
    }
    private static void handleSingle(World world, Action action, SingleEntity on) throws ValueNotInRangeException, EmptyExpressionException {
        Property resultProperty = Utils.findPropertyByName(on, action.getResultPropertyName());
        String newValue = getCalculationResult(world, action, on);

        if (!Utils.validateValueInRange(resultProperty, newValue))
                throw new ValueNotInRangeException(ErrorMessageFormatter.formatActionErrorMessage(action.getType(), action.getEntityName(),
                        action.getResultPropertyName(), "Value is not in property's range and therefore is not set"));

        resultProperty.getValue().setCurrentValue(newValue);
        resultProperty.setStableTime(0);
    }
    private static void performAction(World world, Action action, SingleEntity on) {
        if (Objects.isNull(on))
            handleAll(world, Utils.findEntityByName(world, action.getEntityName()), action);

        else {
            try {
                handleSingle(world, action, on);
            } catch (ValueNotInRangeException | EmptyExpressionException e) {
                Loggers.SIMULATION_LOGGER.info(e.getMessage());
            }
        }
    }
    public static void handle(World world, Action action, SingleEntity on) throws PropertyNotFoundException, EntityNotFoundException {
        if (Objects.isNull(Utils.findEntityByName(world, action.getEntityName())))
            throw new EntityNotFoundException(ErrorMessageFormatter.formatEntityNotFoundMessage(action.getType(), action.getEntityName()));

        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getResultPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getPropertyName()));

        performAction(world, action, on);
    }
}
