package engine.modules;

import engine.consts.PropTypes;
import engine.consts.Restrictions;
import engine.logs.Loggers;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Utils {
    public static Entity findEntityByName(World world, String entityName) {
        return world.getEntities().getEntitiesMap().get(entityName);
    }
    public static Property findAnyPropertyByName(World world, String entityName, String propertyName) {
        Entity mainEntity = Utils.findEntityByName(world, entityName);
        return mainEntity.getInitialProperties().getPropsMap().get(propertyName);
    }
    public static Property findPropertyByName(SingleEntity entity, String propertyName) {
        return entity.getProperties().getPropsMap().get(propertyName);
    }
    public static String getPropertyValueForEntity(SingleEntity singleEntity, String propertyName) {
        return findPropertyByName(singleEntity, propertyName).getValue().getCurrentValue();
    }
    public static void setPropRandomInit(Property property, PRDRange range) {
        switch (property.getType()) {
            case PropTypes.BOOLEAN:
                property.getValue().setInit(String.valueOf(RandomGenerator.randomizeRandomBoolean()));
                break;
            case PropTypes.DECIMAL:
                property.getValue().setInit(String.valueOf(RandomGenerator.randomizeRandomNumber((int) range.getFrom(), (int) range.getTo())));
                break;
            case PropTypes.FLOAT:
                property.getValue().setInit(String.valueOf(RandomGenerator.randomizeFloat((float) range.getFrom(), (float) range.getTo())));
                break;
            case PropTypes.STRING:
                property.getValue().setInit(RandomGenerator.randomizeRandomString(Restrictions.MAX_RANDOM_STRING_LENGTH));
                break;
        }

        property.getValue().setCurrentValue(property.getValue().getInit());
    }
    public static boolean validateValueInRange(Property property, String newValue) {
        if (!Objects.isNull(property.getRange())) {
            if (Float.parseFloat(newValue) > property.getRange().getTo()
                    || Float.parseFloat(newValue) < property.getRange().getFrom()) {
                Loggers.SIMULATION_LOGGER.info(String.format("Can't change property [%s]" +
                                " value from [%s] to [%s] due to range restriction",
                        property.getName(), property.getValue().getCurrentValue(), newValue));

                return false;
            }
        }

        return true;
    }
    public static boolean isDecimal(String str) {
        try {
            Float.parseFloat(str);
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }
    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss").format(date);
    }
}
