package engine.validators.env;

import engine.modules.Constants;
import engine.prototypes.jaxb.PRDEnvProperty;

public class PRDEnvPropertyValidators {
    public static boolean validatePropetyType(PRDEnvProperty property) {
        return Constants.PRD_ENV_PROPERTY_ALLOWED_TYPES.contains(property.getType());
    }
    public static boolean validateTypeForRangeExistance(PRDEnvProperty property) {
        return Constants.PRD_ENV_PROPERTY_RANGE_ALLOWED_TYPES.contains(property.getType());
    }
}
