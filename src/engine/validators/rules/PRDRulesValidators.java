package engine.validators.rules;

import engine.exceptions.*;
import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDRule;
import engine.prototypes.jaxb.PRDRules;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.PRDPropertyValidators;
import engine.validators.actions.PRDActionsValidators;

import java.util.List;
import java.util.stream.Collectors;

public class PRDRulesValidators {
    public static boolean validateRules(PRDWorld world, PRDRules rules) throws PropertyNotFoundException, PRDThenNotFoundException, EntityNotFoundException, InvalidTypeException, UniqueNameException, WhitespacesFoundException {
        return validateRulesUniqueNames(rules)
                && validateNoWhitespacesInNames(rules)
                && validateActions(world, rules);
    }
    private static boolean validateRulesUniqueNames(PRDRules rules) throws UniqueNameException {
        List<String> names = rules.getPRDRule()
                .stream()
                .map(PRDRule::getName)
                .collect(Collectors.toList());

        for (PRDRule rule : rules.getPRDRule())
            if (!PRDPropertyValidators.validateUniqueName(names, rule.getName()))
                throw new UniqueNameException(String.format("Rule name [%s] already exists", rule.getName()));

        return true;
    }
    private static boolean validateNoWhitespacesInNames(PRDRules rules) throws WhitespacesFoundException {
        for (PRDRule rule : rules.getPRDRule())
            if (rule.getName().contains(" "))
                throw new WhitespacesFoundException(String.format("Rule name [%s] contains whitespaces",
                        rule.getName()));

        return true;
    }
    private static boolean validateActions(PRDWorld world, PRDRules rules) throws PropertyNotFoundException, PRDThenNotFoundException, EntityNotFoundException, InvalidTypeException {
        for (PRDRule rule : rules.getPRDRule())
            PRDActionsValidators.validateActions(world, rule.getPRDActions().getPRDAction());

        return true;
    }
}
