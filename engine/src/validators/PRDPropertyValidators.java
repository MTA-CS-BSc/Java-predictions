package validators;

import consts.Restrictions;
import exceptions.*;
import prototypes.prd.generated.PRDEntity;
import prototypes.prd.generated.PRDProperty;
import prototypes.prd.generated.PRDRange;
import types.PropTypes;
import types.TypesUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class PRDPropertyValidators {
    public static boolean validateProperties(PRDEntity entity) throws Exception {
        return validatePropsUniqueNames(entity)
                && validateNoWhitespacesInNames(entity)
                && validatePropsTypes(entity)
                && validateRanges(entity)
                && validatePropsValueAgainstType(entity)
                && validatePropsValuesInRanges(entity)
                && validateInitExistsOnNonRandomProps(entity);
    }

    private static boolean validatePropValue(PRDProperty property) {
        if (property.getPRDValue().isRandomInitialize())
            return true;

        String init = property.getPRDValue().getInit();

        switch (property.getType()) {
            case PropTypes.DECIMAL:
                return TypesUtils.isDecimal(init);
            case PropTypes.BOOLEAN:
                return TypesUtils.isBoolean(init);
            case PropTypes.FLOAT:
                return TypesUtils.isFloat(init);
        }

        return true;
    }

    private static boolean validatePropsValueAgainstType(PRDEntity entity) throws Exception {
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty())
            if (!validatePropValue(property))
                throw new ValueNotInRangeException(String.format("Entity [%s]: Property [%s] type & value do not match",
                        entity.getName(), property.getPRDName()));

        return true;
    }

    private static boolean validatePropsTypes(PRDEntity entity) throws InvalidTypeException {
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

        for (PRDProperty property : notRandomProps)
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
            if (!validatePropValueInRange(property))
                throw new InvalidTypeException(String.format("Entity [%s]: Property [%s]: has range but type is not decimal/float",
                        entity.getName(), property.getPRDName()));
        }

        return true;
    }

    private static boolean validatePropValueInRange(PRDProperty property) {
        PRDRange range = property.getPRDRange();

        if (PropTypes.NUMERIC_PROPS.contains(property.getType()) && !Objects.isNull(range)
                && !property.getPRDValue().isRandomInitialize())
            return Float.parseFloat(property.getPRDValue().getInit()) - range.getFrom() >= 0 &&
                    range.getTo() - Float.parseFloat(property.getPRDValue().getInit()) >= 0;

        return !PropTypes.NUMERIC_PROPS.contains(property.getType())
                || (PropTypes.NUMERIC_PROPS.contains(property.getType()) && property.getPRDValue().isRandomInitialize());
    }
}
