package engine.validators;

import engine.prototypes.jaxb.PRDEvironment;

import java.util.Objects;

public class PRDEvironmentValidators {
    public static boolean validatePropsUniqueNames(PRDEvironment env) {
        return env.getPRDEnvProperty()
                .stream()
                .allMatch(property -> PRDEnvPropertyValidators.validateUniquePropertyName(env, property));
    }

    public static boolean validatePropsTypes(PRDEvironment env) {
        return env.getPRDEnvProperty()
                .stream()
                .allMatch(PRDEnvPropertyValidators::validatePropetyType);
    }

    public static boolean validateRanges(PRDEvironment env) {
        return env.getPRDEnvProperty()
                .stream()
                .filter(element -> !Objects.isNull(element.getPRDRange()))
                .allMatch(PRDEnvPropertyValidators::validateTypeForRangeExistance);
    }
}
