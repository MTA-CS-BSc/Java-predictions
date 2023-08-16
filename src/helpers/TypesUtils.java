package helpers;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TypesUtils {

    public static boolean isDecimal(String str) {
        String newStr = str;

        if (str.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT))
            newStr = str.split("\\.")[0];

        try {
            Integer.parseInt(newStr);
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }
    public static boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        }

        catch (Exception e) {
            return false;
        }
    }
    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy | hh.mm.ss").format(date);
    }
    public static boolean isBoolean(String str) {
        return str.equals(BoolPropValues.TRUE) || str.equals(BoolPropValues.FALSE);
    }
}
