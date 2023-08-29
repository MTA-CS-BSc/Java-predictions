package helpers;

import com.google.gson.Gson;

public class Constants {
    public static Gson GSON_UTIL = new Gson();
    public static final String REGEX_ONLY_ZEROES_AFTER_DOT = "^\\d+\\.0+$";
    public final static int MIN_RANGE = -999999999;
    public final static int MAX_RANGE = 999999999;
    public final static int MIN_GRID_HEIGHT = 10;
    public final static int MAX_GRID_HEIGHT = 100;
    public final static int NOT_SET = -1;
}
