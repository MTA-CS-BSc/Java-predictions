package engine.validators;

import engine.modules.Constants;
import engine.prototypes.PRDEnvProperty;
import java.util.List;
import java.util.stream.Collectors;

public class PRDEnvPropertyValidators {
    public static boolean validateType(String type) {
        return Constants.PRD_ENV_PROPERTY_ALLOWED_TYPES.contains(type);
    }

    public static boolean validateUniqueName(List<PRDEnvProperty> env, String name) {
        return !env.stream().map(PRDEnvProperty::getPRDName)
                            .collect(Collectors.toList())
                            .contains(name);
    }

    public static boolean validateNameWhiteSpaces(String name) {
        return !name.contains(" ");
    }

    public static boolean validateCanHaveRange(PRDEnvProperty property) {
        return property.getType().equals("decimal") || property.getType().equals("float");
    }

}
