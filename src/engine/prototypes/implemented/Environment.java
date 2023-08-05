package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEnvProperty;

import java.util.HashMap;
import java.util.List;

public class Environment {
    protected HashMap<String, Property> envMap = new HashMap<>();

    public Environment(List<PRDEnvProperty> list) {
        for (PRDEnvProperty property : list)
            envMap.put(property.getPRDName(), new Property(property));
    }

    public HashMap<String, Property> getEnvVars() { return envMap; }

    public void setEnvVars(HashMap<String, Property> value) { envMap = value; }
}
