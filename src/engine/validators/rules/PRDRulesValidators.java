package engine.validators.rules;

import engine.exceptions.UniqueNameException;
import engine.exceptions.WhitespacesFoundException;
import engine.prototypes.jaxb.PRDRule;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.actions.PRDActionsValidators;

public class PRDRulesValidators {
    public static boolean validateRules(PRDWorld world) throws Exception {
        return validateRulesUniqueNames(world)
                && validateNoWhitespacesInNames(world)
                && validateActions(world);
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
}