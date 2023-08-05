package engine.modules;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public final static String XSD_PATH = "../schemas/predictions-v1.xsd";
    public final static List<String> PRD_ENV_PROPERTY_ALLOWED_TYPES = Arrays.asList(PropTypes.DECIMAL,
            PropTypes.FLOAT, PropTypes.BOOLEAN, PropTypes.STRING);
    public final static List<String> PRD_ENV_PROPERTY_RANGE_ALLOWED_TYPES = Arrays.asList(PropTypes.DECIMAL, PropTypes.FLOAT);
    public final static List<String> CONDITION_ALLOWED_OPERATORS = Arrays.asList(Operators.EQUALS, Operators.NOT_EQUALS,
            Operators.BT, Operators.LT);

    public final static int MAX_RANDOM_STRING_LENGTH = 50;

    public final static int MIN_RANGE = -999999999;
    public final static int MAX_RANGE = 999999999;

}
