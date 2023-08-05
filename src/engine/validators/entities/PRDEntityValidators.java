package engine.validators.entities;

import engine.logs.Loggers;
import engine.modules.PropTypes;
import engine.prototypes.jaxb.PRDEntity;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDValue;
import engine.validators.PRDPropertyValidators;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PRDEntityValidators {
    public static boolean validateProperties(PRDEntity entity) {
        return validatePropsUniqueNames(entity)
                && validateNoWhitespacesInPropsNames(entity)
                && validatePropsTypes(entity)
                && validatePropsWithRangeTypes(entity)
                && validatePropertiesValues(entity)
                && validateInitExistsOnNonRandomProps(entity);
    }
    private static boolean validatePropsUniqueNames(PRDEntity entity) {
        List<String> names = entity.getPRDProperties().getPRDProperty()
                            .stream()
                            .map(PRDProperty::getPRDName)
                            .collect(Collectors.toList());

        for (PRDProperty property : entity.getPRDProperties().getPRDProperty()) {
            if (!PRDPropertyValidators.validateUniqueName(names, property.getPRDName())) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("Entity [%s]: name [%s] already exists",
                        entity.getName(), property.getPRDName()));
                return false;
            }
        }

        return true;
    }
    private static boolean validateNoWhitespacesInPropsNames(PRDEntity entity) {
        List<String> names = entity.getPRDProperties().getPRDProperty()
                .stream()
                .map(PRDProperty::getPRDName)
                .collect(Collectors.toList());

        return PRDPropertyValidators.validateNoWhitespacesInNames(PRDProperty.class, names);
    }
    private static boolean validatePropsTypes(PRDEntity entity) {
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty()) {
            if (!PRDPropertyValidators.validatePropetyType(property.getType())) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("Entity [%s] : property [%s] has invalid type [%s]",
                        entity.getName(), property.getPRDName(), property.getType()));
                return false;
            }
        }

        return true;
    }
    private static boolean validatePropsWithRangeTypes(PRDEntity entity) {
        List<PRDProperty> propsWithRange =
                        entity.getPRDProperties().getPRDProperty()
                        .stream()
                        .filter(element -> !Objects.isNull(element.getPRDRange()))
                        .collect(Collectors.toList());

        for (PRDProperty property : propsWithRange) {
            if (!PRDPropertyValidators.validateTypeForRangeExistance(property.getType())) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("Entity [%s] : property [%s] has range with incompatible type",
                        entity.getName(), property.getPRDName()));
                return false;
            }
        }

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

        else if (property.getType().equals(PropTypes.BOOLEAN)) {
            return init.equals("true") || init.equals("false");
        }

        return true;
    }
    private static boolean validatePropertiesValues(PRDEntity entity) {
        for (PRDProperty property : entity.getPRDProperties().getPRDProperty())
            if (!validatePropValue(property)) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("Entity [%s] : Property [%s] has incorrect init value",
                        entity.getName(), property.getPRDName()));
                return false;
            }

        return true;
    }
    private static boolean validateInitExistsOnNonRandomProps(PRDEntity entity) {
        List<PRDProperty> notRandomProps = entity.getPRDProperties().getPRDProperty()
                                        .stream()
                                        .filter(element -> !element.getPRDValue().isRandomInitialize())
                                        .collect(Collectors.toList());

        for (PRDProperty property: notRandomProps) {
            if (!property.getType().equals(PropTypes.STRING) && property.getPRDValue().getInit().isEmpty()) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("Entity [%s] : Property [%s] has no init",
                                                        entity.getName(), property.getPRDName()));
                return false;
            }
        }

        return true;
    }
}
