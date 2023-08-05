package engine.modules;

import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.PRDEntity;
import engine.prototypes.jaxb.PRDWorld;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Utils {
    public static Object findEntityByName(Object world, String name) {
        if (world.getClass() == PRDWorld.class) {
            return ((PRDWorld)world).getPRDEntities().getPRDEntity()
                    .stream()
                    .filter(element -> element.getName().equals(name))
                    .findFirst().orElse(null);
        }

        else if (world.getClass() == World.class)
            return ((World)world).getEntities().getEntitiesMap().get(name);

        return null;
    }
    public static Object findPropertyByName(Object world, String entityName, String prop) {
        if (world.getClass() == PRDWorld.class) {
            return ((PRDEntity) Objects.requireNonNull(findEntityByName(world, entityName)))
                    .getPRDProperties().getPRDProperty()
                    .stream()
                    .filter(element -> element.getPRDName().equals(prop))
                    .findFirst().orElse(null);
        }

        else if (world.getClass() == World.class) {
            return (((World)world).getEntities()
                    .getEntitiesMap()
                    .get(entityName))
                    .getProperties()
                    .getPropsMap()
                    .get(prop);
        }

        return null;
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
