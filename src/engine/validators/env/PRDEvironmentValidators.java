package engine.validators.env;

import engine.exceptions.InvalidTypeException;
import engine.exceptions.UniqueNameException;
import engine.exceptions.WhitespacesFoundException;
import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDEnvProperty;
import engine.prototypes.jaxb.PRDEvironment;
import engine.validators.PRDPropertyValidators;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PRDEvironmentValidators {
    public static boolean validateEnvironment(PRDEvironment env) throws Exception {
        return validatePropsUniqueNames(env) && validatePropsTypes(env)
                    && validateRanges(env) && validateNoWhitespacesInNames(env);
    }
    private static boolean validatePropsUniqueNames(PRDEvironment env) throws UniqueNameException {
        List<String> names = env.getPRDEnvProperty()
                            .stream()
                            .map(PRDEnvProperty::getPRDName)
                            .collect(Collectors.toList());

        for (PRDEnvProperty property : env.getPRDEnvProperty())
            if (!PRDPropertyValidators.validateUniqueName(names, property.getPRDName()))
                throw new UniqueNameException(String.format("Environment property name [%s] already exists", property.getPRDName()));

        return true;
    }
    private static boolean validatePropsTypes(PRDEvironment env) throws InvalidTypeException {
        for (PRDEnvProperty property : env.getPRDEnvProperty())
            if (!PRDPropertyValidators.validatePropetyType(property.getType()))
                throw new InvalidTypeException(String.format("Environment property [%s] has invalid type [%s]",
                                                                property.getPRDName(), property.getType()));

        return true;
    }
    private static boolean validateNoWhitespacesInNames(PRDEvironment env) throws WhitespacesFoundException {
        for (PRDEnvProperty property : env.getPRDEnvProperty())
            if (property.getPRDName().contains(" "))
                throw new WhitespacesFoundException(String.format("Environment property [%s] has whitespaces in it's name",
                        property.getPRDName()));

        return true;
    }
    private static boolean validateRanges(PRDEvironment env) throws Exception {
        List<PRDEnvProperty> propsWithRange =
                                        env.getPRDEnvProperty()
                                        .stream()
                                        .filter(element -> !Objects.isNull(element.getPRDRange()))
                                        .collect(Collectors.toList());

        for (PRDEnvProperty property : propsWithRange) {
            if (!PRDPropertyValidators.validateTypeForRangeExistance(property.getType()))
                throw new InvalidTypeException(String.format("Environment property [%s] has range with incompatible type",
                        property.getPRDName()));

            else if (property.getPRDRange().getTo() - property.getPRDRange().getFrom() <= 0)
                throw new Exception(String.format("Environment property [%s] has invalid range",
                        property.getPRDName()));
        }

        return true;
    }
}
