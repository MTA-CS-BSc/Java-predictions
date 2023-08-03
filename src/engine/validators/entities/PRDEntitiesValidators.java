package engine.validators.entities;

import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDEntities;
import engine.prototypes.jaxb.PRDEntity;
import engine.validators.PRDPropertyValidators;

import java.util.List;
import java.util.stream.Collectors;

public class PRDEntitiesValidators {
    public static boolean validateEntities(PRDEntities entities) {
        return validateEntitiesProperties(entities)
                && validateEntitiesUniqueNames(entities)
                && validateNoWhitespacesInNames(entities);
    }
    private static boolean validateEntitiesUniqueNames(PRDEntities entities) {
        List<String> names = entities.getPRDEntity()
                .stream()
                .map(PRDEntity::getName)
                .collect(Collectors.toList());

        for (PRDEntity entity : entities.getPRDEntity()) {
            if (!PRDPropertyValidators.validateUniqueName(names, entity.getName())) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Entity name [%s] already exists", entity.getName()));
                return false;
            }
        }

        return true;
    }
    private static boolean validateNoWhitespacesInNames(PRDEntities entities) {
        List<String> names = entities.getPRDEntity()
                            .stream()
                            .map(PRDEntity::getName)
                            .collect(Collectors.toList());

        for (String name : names) {
            if (name.contains(" ")) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Entity name [%s] contains whitespaces", name));
                return false;
            }
        }

        return true;
    }
    private static boolean validateEntitiesProperties(PRDEntities entities) {
        for (PRDEntity entity : entities.getPRDEntity()) {
            if (!PRDEntityValidators.validateProperties(entity))
                return false;
        }

        return true;
    }
}
