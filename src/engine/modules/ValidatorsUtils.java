package engine.modules;

import engine.consts.PropTypes;
import engine.consts.SystemFunctions;
import engine.prototypes.jaxb.*;

import java.util.Objects;
import java.util.regex.Pattern;

public class ValidatorsUtils {
    public static PRDEntity findPRDEntityByName(PRDWorld world, String entityName) {
        return world.getPRDEntities().getPRDEntity()
                .stream()
                .filter(element -> element.getName().equals(entityName))
                .findFirst().orElse(null);
    }
    public static PRDProperty findPRDPropertyByName(PRDWorld world, String entityName, String propertyName) {
        PRDEntity entity = findPRDEntityByName(world, entityName);

        if (Objects.isNull(entity))
            return null;

        return entity.getPRDProperties().getPRDProperty()
                .stream()
                .filter(element -> element.getPRDName().equals(propertyName))
                .findFirst().orElse(null);
    }
    public static boolean isSystemFunction(String expression) {
        String trimmed = expression.trim();
        String pattern = "^[A-Za-z]+\\([A-Za-z]+\\)$";

        if (!Pattern.matches(pattern, trimmed))
            return false;

        return SystemFunctions.ALL_SYSTEM_FUNCS.contains(trimmed.substring(0, trimmed.indexOf("(")));
    }
    public static String getSystemFunctionType(String expression) {
        return expression.substring(0, expression.indexOf("("));
    }
    public static boolean validateExpressionType(PRDWorld world, PRDAction action, PRDProperty property) {
        String value = action.getValue().trim();
        PRDProperty expressionEntityProp = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), value);

        if (ValidatorsUtils.isSystemFunction(value)) {
            String systemFunctionType = ValidatorsUtils.getSystemFunctionType(value);

            if (systemFunctionType.equals(SystemFunctions.RANDOM))
                return property.getType().equals(PropTypes.DECIMAL);

            else if (systemFunctionType.equals(SystemFunctions.ENVIRONMENT)) {
                PRDEnvProperty envProp = world.getPRDEvironment().getPRDEnvProperty()
                        .stream()
                        .filter(element -> element.getPRDName().equals(value.substring(value.lastIndexOf("("),
                                value.lastIndexOf(")"))))
                        .findFirst().orElse(null);

                if (Objects.isNull(envProp))
                    return false;

                return envProp.getType().equals(property.getType());
            }
        }

        else if (!Objects.isNull(expressionEntityProp))
            return expressionEntityProp.getType().equals(property.getType());

        else {
            if (Utils.isDecimal(value)) {
                if (value.contains("."))
                    return property.getType().equals(PropTypes.FLOAT);

                return PropTypes.NUMERIC_PROPS.contains(property.getType());
            }

            else if (Utils.isBoolean(value))
                return property.getType().equals(PropTypes.BOOLEAN);
        }

        return property.getType().equals(PropTypes.STRING);
    }

}
