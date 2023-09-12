package helpers.types;

import dtos.PropertyDTO;
import engine.consts.SystemFunctions;
import engine.modules.Utils;
import engine.modules.ValidatorsUtils;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.World;
import helpers.loggers.BoolPropValues;
import helpers.Constants;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public abstract class TypesUtils {

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
            if (isDecimal(str))
                return true;

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
        return str.equalsIgnoreCase(BoolPropValues.TRUE) || str.equalsIgnoreCase(BoolPropValues.FALSE);
    }
    public static String removeExtraZeroes(String value) {
        return value.matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT) ? value.split("\\.")[0] : value;
    }
    public static String getSystemFunctionType(String expression) {
        return expression.substring(0, expression.indexOf("("));
    }
    public static String getExpressionType(World world, String entityName, String expression) {
        Property expressionEntityProp = Utils.findAnyPropertyByName(world, entityName, expression);

        if (ValidatorsUtils.isSystemFunction(expression)) {
            String systemFunctionValue = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

            switch (getSystemFunctionType(expression)) {
                case SystemFunctions.RANDOM:
                case SystemFunctions.TICKS:
                    return PropTypes.DECIMAL;
                case SystemFunctions.PERCENT:
                    return PropTypes.FLOAT;
                case SystemFunctions.ENVIRONMENT:
                    return Utils.findEnvironmentPropertyByName(world, systemFunctionValue).getType();
                case SystemFunctions.EVALUATE:
                    String evaluateEntityName = systemFunctionValue.split("\\.")[0];
                    String evaluatePropName = systemFunctionValue.split("\\.")[1];
                    return Utils.findAnyPropertyByName(world, evaluateEntityName, evaluatePropName).getType();
            }
        }

        else if (!Objects.isNull(expressionEntityProp))
            return expressionEntityProp.getType();

        else if (TypesUtils.isDecimal(expression))
            return PropTypes.DECIMAL;

        else if (TypesUtils.isFloat(expression))
            return PropTypes.FLOAT;

        else if (TypesUtils.isBoolean(expression))
            return PropTypes.BOOLEAN;

        return PropTypes.STRING;
    }
    public static boolean validateType(PropertyDTO property, String value) {
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
