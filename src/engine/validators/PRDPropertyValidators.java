package engine.validators;

import engine.consts.PropTypes;
import engine.consts.Restrictions;
import engine.exceptions.*;
import engine.prototypes.jaxb.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PRDPropertyValidators {
    public static boolean validateProperties(PRDEntity entity) throws Exception {
        return validatePropsUniqueNames(entity)
                && validateNoWhitespacesInNames(entity)
                && validatePropsTypes(entity)
                && validateRanges(entity)
                && validatePropsValueAgainstType(entity)
                && validatePropsValuesInRanges(entity)
                && validateInitExistsOnNonRandomProps(entity)
                && validatePopulation(entity);
    }

    public static boolean validatePropsTypes(PRDEntity entity) throws InvalidTypeException {
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty())
            if (!Restrictions.PRD_PROPERTY_ALLOWED_TYPES.contains(property.getType()))
                throw new InvalidTypeException(String.format("Entity [%s]: Property [%s] has invalid type",
                        entity.getName(), property.getPRDName()));

        return true;
    }
    private static boolean validatePropsUniqueNames(PRDEntity entity) throws UniqueNameException {
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty())
            if (entity.getPRDProperties().getPRDProperty()
                    .stream()
                    .filter(element -> element.getPRDName().equals(property.getPRDName()))
                    .count() > 1)
                throw new UniqueNameException(String.format("Entity [%s]: Property name [%s] already exists",
                        entity.getName(),
                        property.getPRDName()));

        return true;
    }
    private static boolean validateNoWhitespacesInNames(PRDEntity entity) throws WhitespacesFoundException {
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty())
            if (property.getPRDName().contains(" "))
                throw new WhitespacesFoundException(String.format("Entity [%s]: Property [%s] has whitespaces in it's name",
                        entity.getName(),
                        property.getPRDName()));

        return true;
    }
    private static boolean validatePopulation(PRDEntity entity) throws ValueNotInRangeException {
        if (entity.getPRDPopulation() <= 0)
            throw new ValueNotInRangeException("Entity [%s]: Population must be positive");

        return true;
    }
    private static boolean validateRanges(PRDEntity entity) throws Exception {
        List<PRDProperty> propsWithRange =
                entity.getPRDProperties().getPRDProperty()
                        .stream()
                        .filter(element -> !Objects.isNull(element.getPRDRange()))
                        .collect(Collectors.toList());

        for (PRDProperty property : propsWithRange) {
            if (!Restrictions.PRD_PROPERTY_RANGE_ALLOWED_TYPES.contains(property.getType()))
                throw new InvalidTypeException(String.format("Entity [%s]: Property [%s] has range with incompatible type",
                        entity.getName(), property.getPRDName()));

            else if (property.getPRDRange().getTo() - property.getPRDRange().getFrom() <= 0)
                throw new Exception(String.format("Entity [%s]: Property [%s] has invalid range",
                        entity.getName(), property.getPRDName()));
        }

        return true;
    }
    private static boolean validateInitExistsOnNonRandomProps(PRDEntity entity) throws EmptyExpressionException {
        List<PRDProperty> notRandomProps = entity.getPRDProperties().getPRDProperty()
                .stream()
                .filter(element -> !element.getPRDValue().isRandomInitialize())
                .collect(Collectors.toList());

        for (PRDProperty property: notRandomProps)
            if (!property.getType().equals(PropTypes.STRING) && property.getPRDValue().getInit().isEmpty())
                throw new EmptyExpressionException(String.format("Entity [%s]: Property [%s] has no init value but is not random",
                        entity.getName(), property.getPRDName()));

        return true;
    }
    private static boolean validatePropsValuesInRanges(PRDEntity entity) throws InvalidTypeException {
        List<PRDProperty> propsWithRange = entity.getPRDProperties().getPRDProperty()
                .stream()
                .filter(property -> !Objects.isNull(property.getPRDRange()))
                .collect(Collectors.toList());

        for (PRDProperty property : propsWithRange) {
            if (!validatePropValueInRange(entity, property))
                throw new InvalidTypeException(String.format("Entity [%s]: Property [%s]: has range but type is not decimal/float",
                        entity.getName(), property.getPRDName()));
        }

        return true;
    }
    private static boolean validatePropValueInRange(PRDEntity entity, PRDProperty property) {
        PRDRange range = property.getPRDRange();

        if (PropTypes.NUMERIC_PROPS.contains(property.getType()) && !Objects.isNull(range)
                && !property.getPRDValue().isRandomInitialize())
            return Float.parseFloat(property.getPRDValue().getInit()) - range.getFrom() >= 0 &&
                    range.getTo() - Float.parseFloat(property.getPRDValue().getInit()) >= 0;

        return false;
    }
}
