package modules;

import javafx.util.Duration;


public abstract class Restrictions {
    public static final String REGEX_ONLY_ZEROES_AFTER_DOT = "^\\d+\\.0+$";
    public final static int MIN_RANGE = -999999999;
    public final static int MAX_RANGE = 999999999;
    public final static int MIN_GRID_LINEAR_SIZE = 10;
    public final static int MAX_GRID_LINEAR_SIZE = 100;
    public final static int NOT_SET = -1;
    public final static String STRING_ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!?,-.()0123456789 ";
    public final static int MAX_RANDOM_STRING_LENGTH = 50;
    public final static Duration ANIMATION_DURATION = new javafx.util.Duration(1000);
}
