package engine.modules;

import engine.consts.PropTypes;
import engine.consts.Restrictions;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Utils {
    public static Entity findEntityByName(World world, String entityName) {
        return world.getEntities().getEntitiesMap().get(entityName);
    }
    public static Property findPropertyByName(World world, String entityName, String propertyName) {
        SingleEntity someEntity = Utils.findEntityByName(world, entityName).getSingleEntities().get(0);

        if (Objects.isNull(someEntity))
            return null;

        return someEntity.getProperties().getPropsMap().get(propertyName);
    }
    public static Property findPropertyByName(SingleEntity entity, String propertyName) {
        return entity.getProperties().getPropsMap().get(propertyName);
    }
    public static PRDEntity findPRDEntityByName(PRDWorld world, String entityName) {
        return world.getPRDEntities().getPRDEntity()
                .stream()
                .filter(element -> element.getName().equals(entityName))
                .findFirst().orElse(null);
    }
    public static PRDProperty findPRDPropertyByName(PRDWorld world, String entityName, String propertyName) {
        PRDEntity entity = findPRDEntityByName(world, entityName);

        if (Objects.isNull(entity))
            return null;

        return entity.getPRDProperties().getPRDProperty()
                .stream()
                .filter(element -> element.getPRDName().equals(propertyName))
                .findFirst().orElse(null);
    }
    public static String getPropertyValueForEntity(SingleEntity singleEntity, String propertyName) {
        return singleEntity.getProperties().getPropsMap().get(propertyName).getValue().getCurrentValue();
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
    public static void setPropRandomInit(Property property, PRDRange range) {
        if (property.getType().equals(PropTypes.BOOLEAN))
            property.getValue().setInit(String.valueOf(RandomGenerator.randomizeRandomBoolean()));

        else if (property.getType().equals(PropTypes.DECIMAL))
            property.getValue().setInit(String.valueOf(RandomGenerator.randomizeRandomNumber((int)range.getFrom(), (int)range.getTo())));

        else if (property.getType().equals(PropTypes.FLOAT))
            property.getValue().setInit(String.valueOf(RandomGenerator.randomizeFloat((float)range.getFrom(), (float)range.getTo())));

        else if (property.getType().equals(PropTypes.STRING))
            property.getValue().setInit(RandomGenerator.randomizeRandomString(Restrictions.MAX_RANDOM_STRING_LENGTH));

        property.getValue().setCurrentValue(property.getValue().getInit());
    }
}
