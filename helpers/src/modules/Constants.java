package modules;

import javafx.util.Duration;


public abstract class Constants {
    public static final String REGEX_ONLY_ZEROES_AFTER_DOT = "^\\d+\\.0+$";
    public final static int MIN_RANGE = -999999999;
    public final static int MAX_RANGE = 999999999;
    public final static int MIN_GRID_LINEAR_SIZE = 10;
    public final static int MAX_GRID_LINEAR_SIZE = 100;
    public final static int NOT_SET = -1;
    public final static String STRING_ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!?,-.()0123456789 ";
    public final static int MAX_RANDOM_STRING_LENGTH = 50;
    public final static Duration ANIMATION_DURATION = new javafx.util.Duration(1000);
    public final static int API_RESPONSE_OK = 200;
    public final static int API_RESPONSE_SERVER_ERROR = 500;
    public final static int API_RESPONSE_BAD_REQUEST = 400;
    public final static int API_REFETCH_INTERVAL_MILLIS = 1500;
}
