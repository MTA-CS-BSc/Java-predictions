package engine.validators;

import engine.modules.Constants;
import engine.prototypes.jaxb.PRDEnvProperty;
import engine.prototypes.jaxb.PRDEvironment;

public class PRDEnvPropertyValidators {
    public static boolean validatePropetyType(PRDEnvProperty property) {
        return Constants.PRD_ENV_PROPERTY_ALLOWED_TYPES.contains(property.getType());
    }

    public static boolean validateUniquePropertyName(PRDEvironment env, PRDEnvProperty property) {
        return env.getPRDEnvProperty()
                .stream()
                .noneMatch(element -> element.getPRDName()
                .equals(property.getPRDName()));
    }

    public static boolean validateTypeForRangeExistance(PRDEnvProperty property) {
        return Constants.PRD_ENV_PROPERTY_RANGE_ALLOWED_TYPES.contains(property.getType());
    }
}
