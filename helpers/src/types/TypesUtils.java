package types;

import modules.Restrictions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public abstract class TypesUtils {

    public static boolean isDecimal(String str) {
        String newStr = str;

        if (str.matches(Restrictions.REGEX_ONLY_ZEROES_AFTER_DOT))
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
        return BoolPropValues.BOOLEAN_PROPS.contains(str);
    }

    public static String removeExtraZeroes(String value) {
        return value.matches(Restrictions.REGEX_ONLY_ZEROES_AFTER_DOT) ? value.split("\\.")[0] : value;
    }

    public static String getSystemFunctionType(String expression) {
        return expression.substring(0, expression.indexOf("("));
    }

    public static boolean validateType(String propertyType, String value) {
        if (propertyType.equals(PropTypes.STRING))
            return true;

        if (propertyType.equals(PropTypes.BOOLEAN))
            return BoolPropValues.BOOLEAN_PROPS.contains(value);

        else if (propertyType.equals(PropTypes.FLOAT))
            return TypesUtils.isFloat(value);

        return propertyType.equals(PropTypes.DECIMAL)
                && TypesUtils.isDecimal(TypesUtils.removeExtraZeroes(value));
    }

    public static boolean isNullOrEmpty(String str) {
        return Objects.isNull(str) || str.isEmpty();
    }
}
