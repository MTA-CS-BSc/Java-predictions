package types;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class PropTypes {
    public static final String DECIMAL = "decimal";
    public static final String BOOLEAN = "boolean";
    public static final String FLOAT = "float";
    public static final String STRING = "string";
    public static final List<String> NUMERIC_PROPS = Arrays.asList(DECIMAL, FLOAT);
    public static final List<String> BOOLEAN_PROPS = Collections.singletonList(BOOLEAN);
    public static final List<String> STRING_PROPS = Collections.singletonList(STRING);
}
