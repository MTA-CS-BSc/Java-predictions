package types;

import modules.Constants;
import other.PropertyDTO;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public abstract class TypesUtils {

    public static boolean isDecimal(String str) {
        String newStr = str;

        if (str.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT))
            newStr = str.split("\\.")[0];

        try {
            Integer.parseInt(newStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isFloat(String str) {
        try {
            if (isDecimal(str))
                return true;

            Float.parseFloat(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd-MM-yyyy | HH:mm:ss").format(date);
    }

    public static boolean isBoolean(String str) {
        return str.equalsIgnoreCase(BoolPropValues.TRUE) || str.equalsIgnoreCase(BoolPropValues.FALSE);
    }

    public static String removeExtraZeroes(String value) {
        return value.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT) ? value.split("\\.")[0] : value;
    }

    public static String getSystemFunctionType(String expression) {
        return expression.substring(0, expression.indexOf("("));
    }

    public static boolean validateType(PropertyDTO property, String value) {
        //TODO: Check if should validate length & charset
        if (property.getType().equals(PropTypes.STRING))
            return true;

        if (property.getType().equals(PropTypes.BOOLEAN))
            return Arrays.asList(BoolPropValues.TRUE, BoolPropValues.FALSE).contains(value);

        else if (property.getType().equals(PropTypes.FLOAT))
            return TypesUtils.isFloat(value);

        return property.getType().equals(PropTypes.DECIMAL)
                && TypesUtils.isDecimal(TypesUtils.removeExtraZeroes(value));
    }
}
