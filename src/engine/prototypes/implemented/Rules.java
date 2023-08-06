package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDRule;

import java.util.HashMap;
import java.util.List;

public class Rules {
    protected HashMap<String, Rule> rulesMap = new HashMap<>();
    public Rules(List<PRDRule> list) {
        for (PRDRule rule : list)
            rulesMap.put(rule.getName(), new Rule(rule));
    }
    public HashMap<String, Rule> getRulesMap() { return rulesMap; }
}
