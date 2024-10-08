package simulation.performers;

import exceptions.EmptyExpressionException;
import exceptions.ErrorMessageFormatter;
import exceptions.InvalidTypeException;
import exceptions.PropertyNotFoundException;
import modules.Utils;
import parsers.ExpressionParser;
import prototypes.SingleEntity;
import prototypes.prd.implemented.Divide;
import prototypes.prd.implemented.Multiply;
import prototypes.prd.implemented.Property;
import prototypes.prd.implemented.World;
import prototypes.prd.implemented.actions.CalculationAction;
import modules.Restrictions;
import types.ActionTypes;

import java.util.Objects;

public abstract class CalculationPerformer {
    private static String getCalculationResult(World world, CalculationAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        Multiply multiply = action.getMultiply();
        Divide divide = action.getDivide();

        String arg1 = ExpressionParser.evaluateExpression(world, Objects.isNull(multiply) ? divide.getArg1() : multiply.getArg1(), main, secondary);
        String arg2 = ExpressionParser.evaluateExpression(world, Objects.isNull(multiply) ? divide.getArg2() : multiply.getArg2(), main, secondary);

        if (arg1.isEmpty() || arg2.isEmpty())
            throw new EmptyExpressionException(String.format("Action [%s]: Type [%s]: Arg1 or Arg2 are not valid expressions",
                    action.getType(), Objects.isNull(multiply) ? "Divide" : "Multiply"));

        if (!Objects.isNull(divide) && Float.parseFloat(arg2) == 0)
            throw new InvalidTypeException("Action [%s]: Can't divide by zero!");

        String result = String.valueOf(Objects.isNull(multiply) ? Float.parseFloat(arg1) / Float.parseFloat(arg2)
                : Float.parseFloat(arg1) * Float.parseFloat(arg2));

        return result.matches(Restrictions.REGEX_ONLY_ZEROES_AFTER_DOT) ? result.split("\\.")[0] : result;

    }

    private static void handle(World world, CalculationAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        Property resultProperty = Utils.findPropertyByName(main, action.getResultPropertyName());
        String newValue = getCalculationResult(world, action, main, secondary);
        ActionsPerformer.setPropertyValue(ActionTypes.CALCULATION, action.getEntityName(), resultProperty, newValue);
    }

    public static void performAction(World world, CalculationAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        if (Objects.isNull(Utils.findAnyPropertyByName(world, action.getEntityName(), action.getResultPropertyName())))
            throw new PropertyNotFoundException(ErrorMessageFormatter.formatPropertyNotFoundMessage(action.getType(), action.getEntityName(), action.getResultPropertyName()));

        handle(world, action, main, secondary);
    }
}
