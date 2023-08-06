package engine.simulation.performers;

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
    private static String getCalculationResult(World world, Action action, SingleEntity on) {
        Multiply multiply = action.getMultiply();
        Divide divide = action.getDivide();

        String arg1 = ExpressionParser.parseExpression(world, action, Objects.isNull(multiply) ? divide.getArg1() : multiply.getArg1());
        String arg2 = ExpressionParser.parseExpression(world, action, Objects.isNull(multiply) ? divide.getArg2() : multiply.getArg2());

        if (arg1.isEmpty() || arg2.isEmpty())
            return "";

        String eval_arg1 = ExpressionParser.evaluateExpression(arg1, on);
        String eval_arg2 = ExpressionParser.evaluateExpression(arg2, on);

        return String.valueOf(Objects.isNull(multiply) ? Float.parseFloat(eval_arg1) / Float.parseFloat(eval_arg2)
                : Float.parseFloat(eval_arg1) * Float.parseFloat(eval_arg2));
    }
    private static void handleAll(World world, Entity mainEntity, Action action) {
        mainEntity.getSingleEntities().forEach(entity -> {
            Property resultProperty = Utils.findPropertyByName(entity, action.getResultPropertyName());
            String newValue = getCalculationResult(world, action, entity);
            resultProperty.getValue().setCurrentValue(newValue);
            resultProperty.setStableTime(0);
        });
    }
    private static void handleSingle(World world, Action action, SingleEntity on) {
        Property resultProperty = Utils.findPropertyByName(on, action.getResultPropertyName());
        String newValue = getCalculationResult(world, action, on);
        resultProperty.getValue().setCurrentValue(newValue);
        resultProperty.setStableTime(0);
    }
    public static void handle(World world, Action action, SingleEntity on) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());
        Property someResultProp = Utils.findAnyPropertyByName(world, action.getEntityName(), action.getResultPropertyName());

        if (Objects.isNull(mainEntity) || Objects.isNull(someResultProp)) {
            Loggers.SIMULATION_LOGGER.info(String.format("In calculation action," +
                    " entity [%s] or result prop [%s] not found", action.getEntityName(), action.getResultPropertyName()));
            return;
        }

        if (Objects.isNull(on))
            handleAll(world, mainEntity, action);

        else
            handleSingle(world, action, on);

        Loggers.SIMULATION_LOGGER.info("Calculation evaluate");
    }

}
