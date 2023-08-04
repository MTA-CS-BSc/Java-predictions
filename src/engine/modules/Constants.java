package engine.modules;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public final static String XSD_PATH = "../schemas/predictions-v1.xsd";
    public final static List<String> PRD_ENV_PROPERTY_ALLOWED_TYPES = Arrays.asList("decimal", "float", "boolean", "string");
    public final static List<String> PRD_ENV_PROPERTY_RANGE_ALLOWED_TYPES = Arrays.asList("decimal", "float");
    public final static List<String> CONDITION_ALLOWED_OPERATORS = Arrays.asList("=", "!=", "bt", "lt");

}
