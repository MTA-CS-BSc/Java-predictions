package engine.modules;

import java.util.Arrays;
import java.util.List;

public class SystemFunctions {
    public static final String RANDOM = "random";
    public static final String ENVIRONMENT = "environment";
    public static final String EVALUATE = "evaluate";
    public static final String PRECENT = "precent";
    public static final String TICKS = "ticks";
    public static final List<String> ALL_SYSTEM_FUNCS =
            Arrays.asList(RANDOM, ENVIRONMENT, EVALUATE, PRECENT, TICKS);
}
