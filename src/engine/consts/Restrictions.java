package engine.consts;

import helpers.types.PropTypes;

import java.util.Arrays;
import java.util.List;

public abstract class Restrictions {
    public final static List<String> PRD_PROPERTY_ALLOWED_TYPES = Arrays.asList(PropTypes.DECIMAL,
            PropTypes.FLOAT, PropTypes.BOOLEAN, PropTypes.STRING);
    public final static List<String> PRD_PROPERTY_RANGE_ALLOWED_TYPES = Arrays.asList(PropTypes.DECIMAL, PropTypes.FLOAT);
}
