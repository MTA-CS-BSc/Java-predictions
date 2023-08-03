package engine.validators.actions;

import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDEntity;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.runtime.JSType.isNumber;

public class PRDActionValidators {
    private static boolean validateIncreaseOrDecreaseAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity())
                && validatePropExists(world, action, action.getProperty())
                && validateExpression(world, action, action.getBy());
    }
    private static boolean validatePropExists(PRDWorld world, PRDAction action, String propName) {
        List<PRDEntity> foundEntity = world.getPRDEntities().getPRDEntity()
                .stream()
                .filter(entity -> entity.getName().equals(action.getEntity()))
                .collect(Collectors.toList());

        List<PRDProperty> props = foundEntity.get(0).getPRDProperties().getPRDProperty()
                .stream()
                .filter(prop -> prop.getPRDName().equals(propName))
                .collect(Collectors.toList());

        if (props.size() == 0)
            Loggers.XML_ERRORS_LOGGER.trace(String.format("Action requested prop name [%s] which does not exist",
                    propName));

        return props.size() > 0;
    }
    private static boolean validateKillAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity());
    }
    private static boolean isSystemFunction(String type) {
        return Arrays.asList("random", "environment").contains(type);
    }
    private static boolean validateRandomProps(String props) {
        return isNumber(Arrays.asList(props.split(",")).get(0));
    }
    private static boolean validateEnvProps(PRDWorld world, String props) {
        return world.getPRDEvironment().getPRDEnvProperty()
                .stream()
                .anyMatch(property -> property.getPRDName().equals(Arrays.asList(props.split(",")).get(0)));
    }
    private static boolean validateExpression(PRDWorld world, PRDAction action, String expression) {
        if (expression.contains("(")) {
            String type = expression.substring(0, expression.indexOf("("));
            String props = expression.substring(expression.indexOf("(") + 1, expression.lastIndexOf(")"));

            if (isSystemFunction(type)) {
                switch (type) {
                    case "random":
                        return validateRandomProps(props);
                    case "environment":
                        return validateEnvProps(world, props);
                }
            }

            else {
                // Check if name is property of main entity
                if (validatePropExists(world, action, action.getProperty()))
                    return true;

                else if (type.equalsIgnoreCase("condition"))
                    return props.equalsIgnoreCase("true") || props.equalsIgnoreCase("false");

                else if (type.equalsIgnoreCase("calculation")
                            || type.equalsIgnoreCase("increase")
                                || type.equalsIgnoreCase("decrease")) {
                    String[] splitted = props.split(",");

                    for (String prop: splitted) {
                        try {
                            Float.parseFloat(prop);
                        }

                        catch (Exception e) {
                            Loggers.XML_ERRORS_LOGGER.trace(String.format("Wrong props [%s] passed to action",
                                    prop));
                            return false;
                        }
                    }

                    return true;
                }
            }
        }

        return true;
    }
    private static boolean validateSetAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity())
                && validatePropExists(world, action, action.getProperty())
                && validateExpression(world, action, action.getValue());
    }
    private static boolean validateEntityExists(PRDWorld world, String entityName) {
        boolean flag = world.getPRDEntities().getPRDEntity()
                .stream()
                .anyMatch(element -> element.getName().equals(entityName));

        if (!flag)
            Loggers.XML_ERRORS_LOGGER.trace(String.format("Action requested entity name [%s] which doesn't exist", entityName));

        return flag;
    }
    private static boolean validateCalc(PRDWorld world, PRDAction action) {
        String multiply_arg1 = action.getPRDMultiply().getArg1();
        String multiply_arg2 = action.getPRDMultiply().getArg2();

        String divide_arg1 = action.getPRDDivide().getArg1();
        String divide_arg2 = action.getPRDDivide().getArg2();

        return validateExpression(world, action, multiply_arg1) && validateExpression(world, action, multiply_arg2)
                    && validateExpression(world, action, divide_arg1) && validateExpression(world, action, divide_arg2);
    }
    private static boolean validateCalculationAction(PRDWorld world, PRDAction action) {
        return validateEntityExists(world, action.getEntity())
                && validatePropExists(world, action, action.getResultProp())
                && validateCalc(world, action);
    }
    private static boolean validateConditionAction(PRDWorld world, PRDAction action) {
        return true;
        //todo: Finish
    }
    public static boolean validateAction(PRDWorld world, PRDAction action) {
        String type = action.getType();

        if (type.equalsIgnoreCase("increase")
            || type.equalsIgnoreCase("decrease"))
            return validateIncreaseOrDecreaseAction(world, action);

        else if (type.equalsIgnoreCase("calculation"))
            return validateCalculationAction(world, action);

        else if (type.equalsIgnoreCase("set"))
            return validateSetAction(world, action);

        else if (type.equalsIgnoreCase("condition"))
            return validateConditionAction(world, action);

        else if (type.equalsIgnoreCase("kill"))
            return validateKillAction(world, action);

        return false;
    }
}
