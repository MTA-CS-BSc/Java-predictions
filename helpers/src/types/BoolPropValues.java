package types;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BoolPropValues {
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    public static final List<String> BOOLEAN_PROPS = Stream.of(TRUE, FALSE).collect(Collectors.toList());
}
