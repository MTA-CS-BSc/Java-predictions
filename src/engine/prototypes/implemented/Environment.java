package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEnvProperty;

import java.util.HashMap;
import java.util.List;

public class Environment {
    protected HashMap<String, PRDEnvProperty> envMap = new HashMap<>();

    public Environment(List<PRDEnvProperty> list) {
        for (PRDEnvProperty property : list)
            envMap.put(property.getPRDName(), property);
    }

    public HashMap<String, PRDEnvProperty> getEnvVars() { return envMap; }

    public void setEnvVars(HashMap<String, PRDEnvProperty> value) { envMap = value; }
}
