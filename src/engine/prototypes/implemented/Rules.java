package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDRule;

import java.util.HashMap;
import java.util.List;

public class Rules {
    protected HashMap<String, PRDRule> rules = new HashMap<>();

    public Rules(List<PRDRule> list) {
        for (PRDRule rule : list)
            rules.put(rule.getName(), rule);
    }
    public HashMap<String, PRDRule> getRules() { return rules; }
    public void setEntities(HashMap<String, PRDRule> value) { rules = value; }
}
