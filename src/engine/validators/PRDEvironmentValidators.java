package engine.validators;

import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDEnvProperty;
import engine.prototypes.jaxb.PRDEvironment;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PRDEvironmentValidators {
    public static boolean validateEnvironment(PRDEvironment env) {
        return validatePropsUniqueNames(env)
                && validatePropsTypes(env)
                && validateRanges(env)
                && validateNoWhitespacesInNames(env);
    }
    private static boolean validatePropsUniqueNames(PRDEvironment env) {
        for (PRDEnvProperty property : env.getPRDEnvProperty()) {
            if (!PRDEnvPropertyValidators.validateUniquePropertyName(env, property)) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Env property name [%s] already exists", property.getPRDName()));
                return false;
            }
        }

        return true;
    }

    private static boolean validatePropsTypes(PRDEvironment env) {
        boolean isValid = true;

        for (PRDEnvProperty property : env.getPRDEnvProperty()) {
            if (!PRDEnvPropertyValidators.validatePropetyType(property)) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Env property [%s] has invalid type [%s]",
                                                                property.getPRDName(), property.getType()));
                isValid = false;
            }
        }

        return isValid;
    }

    private static boolean validateNoWhitespacesInNames(PRDEvironment env) {
        boolean isValid = true;

        for (PRDEnvProperty property : env.getPRDEnvProperty()) {
            if (!PRDEnvPropertyValidators.validateNameNoWhitespaces(property)) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Env property [%s] contains whitespaces",
                                                                property.getPRDName()));
                isValid = false;
            }
        }

        return isValid;
    }

    private static boolean validateRanges(PRDEvironment env) {
        boolean isValid = true;

        List<PRDEnvProperty> propsWithRange =
                                        env.getPRDEnvProperty()
                                        .stream()
                                        .filter(element -> !Objects.isNull(element.getPRDRange()))
                                        .collect(Collectors.toList());

        for (PRDEnvProperty property : propsWithRange) {
            if (!PRDEnvPropertyValidators.validateTypeForRangeExistance(property)) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Env property [%s] has range with incompatible type",
                                                                  property.getPRDName()));
                isValid = false;
            }
        }

        return isValid;
    }
}
