package helpers.types;

import java.util.Arrays;
import java.util.List;

public abstract class ActionTypes {
    public static final String INCREASE = "increase";
    public static final String DECREASE = "decrease";
    public static final String CALCULATION = "calculation";
    public static final String CONDITION = "condition";
    public static final String SET = "set";
    public static final String KILL = "kill";
    public static final String REPLACE = "replace";
    public static final String PROXIMITY = "proximity";
    public static List<String> NUMERIC_ACTIONS = Arrays.asList(INCREASE, DECREASE, CALCULATION);
    public static List<String> ENTITY_MAY_NOT_EXIST_TYPES = Arrays.asList(ActionTypes.PROXIMITY, ActionTypes.REPLACE, ActionTypes.CONDITION);
}
