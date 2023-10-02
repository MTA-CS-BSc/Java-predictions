package prototypes.implemented;

import prototypes.jaxb.PRDRule;

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

    public Rules(Rules other) {
        rulesMap = new HashMap<>();

        other.getRulesMap().forEach((key, value) -> rulesMap.put(key, new Rule(value)));
    }

    public Map<String, Rule> getRulesMap() {
        return rulesMap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("###########Rules###########\n");

        getRulesMap().values().forEach(rule -> sb.append(rule.toString()).append("\n"));

        return sb.toString();
    }
}
