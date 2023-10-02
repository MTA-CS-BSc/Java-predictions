package validators.rules;

import exceptions.UniqueNameException;
import exceptions.ValueNotInRangeException;
import exceptions.WhitespacesFoundException;
import prototypes.jaxb.PRDRule;
import prototypes.jaxb.PRDWorld;
import validators.actions.PRDActionsValidators;

import java.util.Objects;

public abstract class PRDRulesValidators {
    public static boolean validateRules(PRDWorld world) throws Exception {
        return validateRulesUniqueNames(world)
                && validateNoWhitespacesInNames(world)
                && validateActions(world)
                && validateActivations(world);
    }

    private static boolean validateRulesUniqueNames(PRDWorld world) throws UniqueNameException {
        for (PRDRule rule : world.getPRDRules().getPRDRule())
            if (world.getPRDRules().getPRDRule()
                    .stream()
                    .filter(element -> element.getName().equals(rule.getName()))
                    .count() > 1)
                throw new UniqueNameException(String.format("Rule name [%s] already exists", rule.getName()));

        return true;
    }

    private static boolean validateNoWhitespacesInNames(PRDWorld world) throws WhitespacesFoundException {
        for (PRDRule rule : world.getPRDRules().getPRDRule())
            if (rule.getName().contains(" "))
                throw new WhitespacesFoundException(String.format("Rule name [%s] contains whitespaces",
                        rule.getName()));

        return true;
    }

    private static boolean validateActions(PRDWorld world) throws Exception {
        for (PRDRule rule : world.getPRDRules().getPRDRule())
            PRDActionsValidators.validateActions(world, rule.getPRDActions().getPRDAction());

        return true;
    }

    private static boolean validateActivations(PRDWorld world) throws ValueNotInRangeException {
        for (PRDRule rule : world.getPRDRules().getPRDRule()) {
            if (!Objects.isNull(rule.getPRDActivation())) {
                if (rule.getPRDActivation().getProbability() < 0
                        || rule.getPRDActivation().getProbability() > 1)
                    throw new ValueNotInRangeException(String.format("Rule [%s]: Activation probability should be on the interval [0,1]",
                            rule.getName()));

                if (!Objects.isNull(rule.getPRDActivation().getTicks()))
                    if (rule.getPRDActivation().getTicks() < 0)
                        throw new ValueNotInRangeException(String.format("Rule [%s]: Activation ticks should be positive",
                                rule.getName()));
            }
        }

        return true;
    }
}