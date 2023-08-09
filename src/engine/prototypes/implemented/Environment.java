package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEnvProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {
    protected Map<String, Property> envMap;
    public Environment(List<PRDEnvProperty> list) {
        envMap = new HashMap<>();

        for (PRDEnvProperty property : list)
            envMap.put(property.getPRDName(), new Property(property));
    }
    public Map<String, Property> getEnvVars() { return envMap; }
}
