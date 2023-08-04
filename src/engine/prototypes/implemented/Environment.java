package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEnvProperty;

import java.util.HashMap;
import java.util.List;

public class Environment {
    protected HashMap<String, PRDEnvProperty> environmentVariables = new HashMap<>();

    public Environment(List<PRDEnvProperty> list) {
        for (PRDEnvProperty property : list)
            environmentVariables.put(property.getPRDName(), property);
    }

    public HashMap<String, PRDEnvProperty> getEnvVars() { return environmentVariables; }

    public void setEnvVars(HashMap<String, PRDEnvProperty> value) { environmentVariables = value; }
}
