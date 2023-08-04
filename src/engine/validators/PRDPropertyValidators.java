package engine.validators;

import engine.logs.Loggers;
import engine.modules.Constants;

import java.util.List;

public class PRDPropertyValidators {
    public static boolean validatePropetyType(String type) {
        return Constants.PRD_ENV_PROPERTY_ALLOWED_TYPES.contains(type);
    }
    public static boolean validateUniqueName(List<String> names, String name) {
        int counter = 0;

        for (String s : names)
            if (s.equals(name))
                counter++;

        return counter == 1;
    }
    public static boolean validateTypeForRangeExistance(String type) {
        return Constants.PRD_ENV_PROPERTY_RANGE_ALLOWED_TYPES.contains(type);
    }

    public static boolean validateNoWhitespacesInNames(Class<?> context, List<String> names) {
        String type = context.getSimpleName().substring(3);

        for (String name : names) {
            if (name.contains(" ")) {
                Loggers.XML_ERRORS_LOGGER.trace(String.format("[%s] name [%s] contains whitespaces", type, name));
                return false;
            }
        }

        return true;
    }
}
