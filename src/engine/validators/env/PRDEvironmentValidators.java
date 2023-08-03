package engine.validators.env;

import engine.logs.Loggers;
import engine.modules.Helpers;
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
        List<String> names = env.getPRDEnvProperty()
                            .stream()
                            .map(PRDEnvProperty::getPRDName)
                            .collect(Collectors.toList());

        for (PRDEnvProperty property : env.getPRDEnvProperty()) {
            if (!Helpers.validateUniqueName(names, property.getPRDName())) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Env property name [%s] already exists", property.getPRDName()));
                return false;
            }
        }

        return true;
    }
    private static boolean validatePropsTypes(PRDEvironment env) {
        for (PRDEnvProperty property : env.getPRDEnvProperty()) {
            if (!PRDEnvPropertyValidators.validatePropetyType(property)) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Env property [%s] has invalid type [%s]",
                                                                property.getPRDName(), property.getType()));
                return false;
            }
        }

        return true;
    }
    private static boolean validateNoWhitespacesInNames(PRDEvironment env) {
        for (PRDEnvProperty property : env.getPRDEnvProperty()) {
            if (property.getPRDName().contains(" ")) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Env property [%s] contains whitespaces",
                                                                property.getPRDName()));
                return false;
            }
        }

        return true;
    }
    private static boolean validateRanges(PRDEvironment env) {
        List<PRDEnvProperty> propsWithRange =
                                        env.getPRDEnvProperty()
                                        .stream()
                                        .filter(element -> !Objects.isNull(element.getPRDRange()))
                                        .collect(Collectors.toList());

        for (PRDEnvProperty property : propsWithRange) {
            if (!PRDEnvPropertyValidators.validateTypeForRangeExistance(property)) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Env property [%s] has range with incompatible type",
                                                                  property.getPRDName()));
                return false;
            }
        }

        return true;
    }
}
