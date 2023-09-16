package engine.validators.entities;

import engine.exceptions.UniqueNameException;
import engine.exceptions.WhitespacesFoundException;
import engine.prototypes.jaxb.PRDEntity;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.PRDPropertyValidators;

public abstract class PRDEntitiesValidators {
    public static boolean validateEntities(PRDWorld world) throws Exception {
        return validateEntitiesUniqueNames(world)
                && validateNoWhitespacesInNames(world)
                && validateEntitiesProperties(world);
    }

    private static boolean validateEntitiesUniqueNames(PRDWorld world) throws UniqueNameException {
        for (PRDEntity entity : world.getPRDEntities().getPRDEntity())
            if (world.getPRDEntities().getPRDEntity()
                    .stream()
                    .filter(element -> element.getName().equals(entity.getName()))
                    .count() > 1)
                throw new UniqueNameException(String.format("Entity name [%s] already exists", entity.getName()));

        return true;
    }

    private static boolean validateNoWhitespacesInNames(PRDWorld world) throws WhitespacesFoundException {
        for (PRDEntity entity : world.getPRDEntities().getPRDEntity())
            if (entity.getName().contains(" "))
                throw new WhitespacesFoundException(String.format("Entity [%s] has whitespaces in it's name", entity.getName()));

        return true;
    }

    private static boolean validateEntitiesProperties(PRDWorld world) throws Exception {
        for (PRDEntity entity : world.getPRDEntities().getPRDEntity())
            PRDPropertyValidators.validateProperties(entity);

        return true;
    }
}