package engine.simulation.performers;

import engine.consts.PropTypes;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.Objects;

public class SetPerformer {
    private static void handleAll(Entity mainEntity, String propertyName, String newValue) {
        mainEntity.getSingleEntities().forEach(entity -> {
            Property propToChange = Utils.findPropertyByName(entity, propertyName);

            Loggers.SIMULATION_LOGGER.info(String.format("Changing property [%s]" +
                    " value [%s]->[%s]", propertyName, propToChange.getValue().getCurrentValue(), newValue));

            propToChange.getValue().setCurrentValue(newValue);
            propToChange.setStableTime(0);

        });
    }
    private static void handleSingle(SingleEntity on, String propertyName, String newValue) {
        Property property = Utils.findPropertyByName(on, propertyName);
        property.getValue().setCurrentValue(newValue);
        property.setStableTime(0);
    }
    public static void handle(World world, Action action, SingleEntity on) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());
        String parsedValue = ExpressionParser.parseExpression(world, action, action.getValue());
        Property property = Utils.findAnyPropertyByName(world, action.getEntityName(), action.getPropertyName());
        String newValue = ExpressionParser.evaluateExpression(parsedValue, on);

        if (Objects.isNull(mainEntity) || Objects.isNull(property)
            || (PropTypes.NUMERIC_PROPS.contains(property.getType())
                && !Utils.validateValueInRange(property, newValue)))
            return;

        if (Objects.isNull(on))
            handleAll(mainEntity, action.getPropertyName(), newValue);

        else
            handleSingle(on, action.getPropertyName(), newValue);

        Loggers.SIMULATION_LOGGER.info("Set evaluate");
    }
}
