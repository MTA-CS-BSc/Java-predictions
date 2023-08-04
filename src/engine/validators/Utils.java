package engine.validators;

import engine.prototypes.jaxb.PRDEntity;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;

public class Utils {
    public static PRDEntity findEntityByName(PRDWorld world, String name) {
        return world.getPRDEntities().getPRDEntity()
                .stream()
                .filter(element -> element.getName().equals(name))
                .findFirst().orElse(null);
    }

    public static PRDProperty findPropertyByName(PRDWorld world, String entityName, String prop) {
        return findEntityByName(world, entityName)
                .getPRDProperties().getPRDProperty()
                .stream()
                .filter(element -> element.getPRDName().equals(prop))
                .findFirst().orElse(null);
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
}
