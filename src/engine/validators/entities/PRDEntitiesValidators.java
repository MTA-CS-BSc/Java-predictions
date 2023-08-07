package engine.validators.entities;

import engine.exceptions.*;
import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDEntities;
import engine.prototypes.jaxb.PRDEntity;
import engine.validators.PRDPropertyValidators;

import java.util.List;
import java.util.stream.Collectors;

public class PRDEntitiesValidators {
    public static boolean validateEntities(PRDEntities entities) throws UniqueNameException, WhitespacesFoundException, EmptyExpressionException, ValueNotInRangeException, InvalidTypeException {
        return validateEntitiesProperties(entities)
                && validateEntitiesUniqueNames(entities)
                && validateNoWhitespacesInNames(entities);
    }
    private static boolean validateEntitiesUniqueNames(PRDEntities entities) throws UniqueNameException {
        List<String> names = entities.getPRDEntity()
                .stream()
                .map(PRDEntity::getName)
                .collect(Collectors.toList());

        for (PRDEntity entity : entities.getPRDEntity())
            if (!PRDPropertyValidators.validateUniqueName(names, entity.getName()))
                throw new UniqueNameException(String.format("Entity name [%s] already exists", entity.getName()));

        return true;
    }
    private static boolean validateNoWhitespacesInNames(PRDEntities entities) throws WhitespacesFoundException {
        for (PRDEntity entity : entities.getPRDEntity())
            if (entity.getName().contains(" "))
                throw new WhitespacesFoundException(String.format("Entity [%s] has whitespaces in it's name", entity.getName()));

        return true;
    }
    private static boolean validateEntitiesProperties(PRDEntities entities) throws UniqueNameException, WhitespacesFoundException, EmptyExpressionException, ValueNotInRangeException, InvalidTypeException {
        for (PRDEntity entity : entities.getPRDEntity())
            PRDEntityValidators.validateProperties(entity);

        return true;
    }
}
