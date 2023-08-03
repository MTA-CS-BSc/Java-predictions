package engine.validators.rules;

import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDRule;
import engine.prototypes.jaxb.PRDRules;
import engine.validators.PRDPropertyValidators;

import java.util.List;
import java.util.stream.Collectors;

public class PRDRulesValidators {
    public static boolean validateRules(PRDRules rules) {
        return validateRulesUniqueNames(rules)
                && validateNoWhitespacesInNames(rules);
    }
    private static boolean validateRulesUniqueNames(PRDRules rules) {
        List<String> names = rules.getPRDRule()
                .stream()
                .map(PRDRule::getName)
                .collect(Collectors.toList());

        for (PRDRule rule : rules.getPRDRule()) {
            if (!PRDPropertyValidators.validateUniqueName(names, rule.getName())) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Rule name [%s] already exists",
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

        for (String name : names) {
            if (name.contains(" ")) {
                Loggers.XML_ERRORS_LOGGER.error(String.format("Rule name [%s] contains whitespaces", name));
                return false;
            }
        }

        return true;
    }

    //TODO: Add validations for PRDActions & PRDActivation
}
