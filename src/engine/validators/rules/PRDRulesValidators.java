package engine.validators.rules;

import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDRule;
import engine.prototypes.jaxb.PRDRules;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.PRDPropertyValidators;
import engine.validators.actions.PRDActionsValidators;

import java.util.List;
import java.util.stream.Collectors;

public class PRDRulesValidators {
    public static boolean validateRules(PRDWorld world, PRDRules rules) {
        return validateRulesUniqueNames(rules)
                && validateNoWhitespacesInNames(rules)
                && validateActions(world, rules);
    }
    private static boolean validateRulesUniqueNames(PRDRules rules) {
        List<String> names = rules.getPRDRule()
                .stream()
                .map(PRDRule::getName)
                .collect(Collectors.toList());

        for (PRDRule rule : rules.getPRDRule()) {
            if (!PRDPropertyValidators.validateUniqueName(names, rule.getName())) {
                Loggers.XML_ERRORS_LOGGER.info(String.format("Rule name [%s] already exists",
                                                            rule.getName()));
                return false;
            }
        }

        return true;
    }
    private static boolean validateNoWhitespacesInNames(PRDRules rules) {
        List<String> names = rules.getPRDRule()
                .stream()
                .map(PRDRule::getName)
                .collect(Collectors.toList());

        return PRDPropertyValidators.validateNoWhitespacesInNames(PRDRule.class, names);
    }
    private static boolean validateActions(PRDWorld world, PRDRules rules) {
        for (PRDRule rule : rules.getPRDRule()) {
            if (!PRDActionsValidators.validateActions(world, rule.getPRDActions().getPRDAction()))
                return false;
        }

        return true;
    }
}
