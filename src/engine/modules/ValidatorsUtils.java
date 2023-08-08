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
        String pattern = "^[A-Za-z]{1,}\\([A-Za-z0-9]{1,}\\)$";

        if (!Pattern.matches(pattern, expression))
            return false;

        return SystemFunctions.ALL_SYSTEM_FUNCS.contains(expression.substring(0, expression.indexOf("(")));
    }
    public static String getSystemFunctionType(String expression) {
        return expression.substring(0, expression.indexOf("("));
    }
    public static boolean validateExpressionType(PRDWorld world, PRDAction action, PRDProperty property, String value) {
        PRDProperty expressionEntityProp = ValidatorsUtils.findPRDPropertyByName(world, action.getEntity(), value);

        if (ValidatorsUtils.isSystemFunction(value)) {
            String systemFunctionType = ValidatorsUtils.getSystemFunctionType(value);

            if (systemFunctionType.equals(SystemFunctions.RANDOM))
                return property.getType().equals(PropTypes.DECIMAL) &&
                        Utils.isDecimal(value.substring(value.lastIndexOf("(") + 1, value.lastIndexOf(")")));

            else if (systemFunctionType.equals(SystemFunctions.ENVIRONMENT)) {
                PRDEnvProperty envProp = world.getPRDEvironment().getPRDEnvProperty()
                        .stream()
                        .filter(element -> element.getPRDName().equals(value.substring(value.lastIndexOf("(") + 1,
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
            if (Utils.isDecimal(value))
                return PropTypes.NUMERIC_PROPS.contains(property.getType());

            else if (Utils.isFloat(value))
                return property.getType().equals(PropTypes.FLOAT);

            else if (Utils.isBoolean(value))
                return property.getType().equals(PropTypes.BOOLEAN);
        }

        return property.getType().equals(PropTypes.STRING);
    }

}
