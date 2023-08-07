package engine.validators.entities;

import engine.exceptions.*;
import engine.logs.Loggers;
import engine.consts.BoolPropValues;
import engine.consts.PropTypes;
import engine.prototypes.jaxb.PRDEntity;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDRange;
import engine.prototypes.jaxb.PRDValue;
import engine.validators.PRDPropertyValidators;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PRDEntityValidators {
    public static boolean validateProperties(PRDEntity entity) throws UniqueNameException, WhitespacesFoundException,
            InvalidTypeException, ValueNotInRangeException, EmptyExpressionException {
        return validatePropsUniqueNames(entity)
                && validateNoWhitespacesInPropsNames(entity)
                && validatePropsTypes(entity)
                && validatePropsWithRangeTypes(entity)
                && validatePropertiesValues(entity)
                && validateInitExistsOnNonRandomProps(entity);
    }
    private static boolean validatePropsUniqueNames(PRDEntity entity) throws UniqueNameException {
        List<String> names = entity.getPRDProperties().getPRDProperty()
                            .stream()
                            .map(PRDProperty::getPRDName)
                            .collect(Collectors.toList());

        for (PRDProperty property : entity.getPRDProperties().getPRDProperty())
            if (!PRDPropertyValidators.validateUniqueName(names, property.getPRDName()))
                throw new UniqueNameException(String.format("Entity [%s]: Property [%s] already exists",
                        entity.getName(), property.getPRDName()));

        return true;
    }
    private static boolean validateNoWhitespacesInPropsNames(PRDEntity entity) throws WhitespacesFoundException {
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty())
            if (property.getPRDName().contains(" "))
                throw new WhitespacesFoundException(String.format("Entity [%s]: Property name [%s] contains whitespaces",
                        entity.getName(), property.getPRDName()));

        return true;
    }
    private static boolean validatePropsTypes(PRDEntity entity) throws InvalidTypeException {
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty())
            if (!PRDPropertyValidators.validatePropetyType(property.getType()))
                throw new InvalidTypeException(String.format("Entity [%s]: Property [%s] has invalid type [%s]",
                        entity.getName(), property.getPRDName(), property.getType()));

        return true;
    }
    private static boolean validatePropsWithRangeTypes(PRDEntity entity) throws InvalidTypeException {
        List<PRDProperty> propsWithRange =
                        entity.getPRDProperties().getPRDProperty()
                        .stream()
                        .filter(element -> !Objects.isNull(element.getPRDRange()))
                        .collect(Collectors.toList());

        for (PRDProperty property : propsWithRange)
            if (!PRDPropertyValidators.validateTypeForRangeExistance(property.getType()))
                throw new InvalidTypeException(String.format("Entity [%s]: Property [%s] has range with incompatible type",
                        entity.getName(), property.getPRDName()));

        return true;
    }
    private static boolean validatePropValueAgainstRange(PRDProperty property) {
        PRDRange range = property.getPRDRange();

        if (PropTypes.NUMERIC_PROPS.contains(property.getType()) && !Objects.isNull(range)
                && !property.getPRDValue().isRandomInitialize())
            return Float.parseFloat(property.getPRDValue().getInit()) - range.getFrom() >= 0 &&
                    range.getTo() - Float.parseFloat(property.getPRDValue().getInit()) >= 0;

        return true;

    }
    private static boolean validatePropValue(PRDProperty property) {
        PRDValue value = property.getPRDValue();
        if (value.isRandomInitialize())
            return true;

        String init = value.getInit();

        if (property.getType().equals(PropTypes.DECIMAL) || property.getType().equals(PropTypes.FLOAT)) {
            try {
                Float.parseFloat(init);
            }

            catch (Exception e) {
                return false;
            }
        }

        else if (property.getType().equals(PropTypes.BOOLEAN))
            return init.equals(BoolPropValues.TRUE) || init.equals(BoolPropValues.FALSE);

        return true;
    }
    private static boolean validatePropertiesValues(PRDEntity entity) throws InvalidTypeException, ValueNotInRangeException {
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty()) {
            if (!validatePropValue(property))
                throw new InvalidTypeException(String.format("Entity [%s]: Property [%s] has incorrect init value for it's type",
                        entity.getName(), property.getPRDName()));

            if (!validatePropValueAgainstRange(property))
                throw new ValueNotInRangeException(String.format("Entity [%s]: Property [%s] has incorrect init value according to range",
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
}
