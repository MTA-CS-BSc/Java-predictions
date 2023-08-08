package engine.validators.env;

import engine.consts.Restrictions;
import engine.exceptions.InvalidTypeException;
import engine.exceptions.UniqueNameException;
import engine.exceptions.WhitespacesFoundException;
import engine.prototypes.jaxb.PRDEnvProperty;
import engine.prototypes.jaxb.PRDEvironment;
import engine.prototypes.jaxb.PRDWorld;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PRDEnvironmentValidators {
    public static boolean validateEnvironment(PRDWorld world) throws Exception {
        return validatePropsUniqueNames(world) && validatePropsTypes(world)
                && validateRanges(world) && validateNoWhitespacesInNames(world);
    }
    private static boolean validatePropsUniqueNames(PRDWorld world) throws UniqueNameException {
        for (PRDEnvProperty property : world.getPRDEvironment().getPRDEnvProperty())
            if (world.getPRDEvironment().getPRDEnvProperty()
                    .stream()
                    .filter(element -> element.getPRDName().equals(property.getPRDName()))
                    .count() > 1)
                throw new UniqueNameException(String.format("Environment property name [%s] already exists", property.getPRDName()));

        return true;
    }
    private static boolean validatePropsTypes(PRDWorld world) throws InvalidTypeException {
        for (PRDEnvProperty property : world.getPRDEvironment().getPRDEnvProperty())
            if (!Restrictions.PRD_PROPERTY_ALLOWED_TYPES.contains(property.getType()))
                throw new InvalidTypeException(String.format("Environment property [%s] has invalid type [%s]",
                        property.getPRDName(), property.getType()));

        return true;
    }
    private static boolean validateNoWhitespacesInNames(PRDWorld world) throws WhitespacesFoundException {
        for (PRDEnvProperty property : world.getPRDEvironment().getPRDEnvProperty())
            if (property.getPRDName().contains(" "))
                throw new WhitespacesFoundException(String.format("Environment property [%s] has whitespaces in it's name",
                        property.getPRDName()));

        return true;
    }
    private static boolean validateRanges(PRDWorld world) throws Exception {
        List<PRDEnvProperty> propsWithRange =
                world.getPRDEvironment().getPRDEnvProperty()
                        .stream()
                        .filter(element -> !Objects.isNull(element.getPRDRange()))
                        .collect(Collectors.toList());

        for (PRDEnvProperty property : propsWithRange) {
            if (!Restrictions.PRD_PROPERTY_RANGE_ALLOWED_TYPES.contains(property.getType()))
                throw new InvalidTypeException(String.format("Environment property [%s] has range with incompatible type",
                        property.getPRDName()));

            else if (property.getPRDRange().getTo() - property.getPRDRange().getFrom() <= 0)
                throw new Exception(String.format("Environment property [%s] has invalid range",
                        property.getPRDName()));
        }

        return true;
    }
}