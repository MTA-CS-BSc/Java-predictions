package engine.simulation.performers;

import engine.logs.Loggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.Objects;

public class DecrementPerformer {
    private static String getDecrementResult(String propValue, String by) {
        return String.valueOf(Float.parseFloat(propValue) - Float.parseFloat(by));
    }
    private static void handleAll(Entity mainEntity, String propertyName, String by) {
        mainEntity.getSingleEntities().forEach(entity -> {
            Property propToChange = Utils.findPropertyByName(entity, propertyName);
            String newValue = getDecrementResult(propToChange.getValue().getCurrentValue(), by);
            propToChange.getValue().setCurrentValue(newValue);
            propToChange.setStableTime(0);
        });
    }
    private static void handleSingle(SingleEntity on, String propertyName, String by) {
        Property propToChange = Utils.findPropertyByName(on, propertyName);
        String newValue = getDecrementResult(propToChange.getValue().getCurrentValue(), by);
        propToChange.getValue().setCurrentValue(newValue);
        propToChange.setStableTime(0);
    }
    public static void handle(World world, Action action, SingleEntity on) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());
        String parsedValue = ExpressionParser.parseExpression(world, action, action.getBy());
        String by = ExpressionParser.evaluateExpression(parsedValue, on);

        if (Objects.isNull(on))
            handleAll(mainEntity, action.getPropertyName(), by);

        else
            handleSingle(on, action.getPropertyName(), by);

        Loggers.SIMULATION_LOGGER.info("Decrease evaluate");
    }
}
