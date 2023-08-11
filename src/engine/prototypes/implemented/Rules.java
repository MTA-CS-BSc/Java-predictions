package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDRule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rules implements Serializable {
    protected Map<String, Rule> rulesMap;
    public Rules(List<PRDRule> list) {
        rulesMap = new HashMap<>();

        for (PRDRule rule : list)
            rulesMap.put(rule.getName(), new Rule(rule));
    }
    public Map<String, Rule> getRulesMap() { return rulesMap; }
}
