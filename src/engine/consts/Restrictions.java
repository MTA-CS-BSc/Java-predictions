package engine.consts;

import java.util.Arrays;
import java.util.List;

public abstract class Restrictions {
    public final static String XSD_PATH = "../schemas/predictions-v1.xsd";
    public final static List<String> PRD_PROPERTY_ALLOWED_TYPES = Arrays.asList(PropTypes.DECIMAL,
            PropTypes.FLOAT, PropTypes.BOOLEAN, PropTypes.STRING);
    public final static List<String> PRD_PROPERTY_RANGE_ALLOWED_TYPES = Arrays.asList(PropTypes.DECIMAL, PropTypes.FLOAT);
    public final static int MAX_RANDOM_STRING_LENGTH = 50;
}
