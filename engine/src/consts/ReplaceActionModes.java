package consts;

import java.util.Arrays;
import java.util.List;

public abstract class ReplaceActionModes {
    public static final String SCRATCH = "scratch";
    public static final String DERIVED = "derived";
    public static final List<String> REPLACE_MODES = Arrays.asList(SCRATCH, DERIVED);
}
