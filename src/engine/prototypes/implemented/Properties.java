package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDProperty;

import java.util.HashMap;
import java.util.List;

public class Properties {
    protected HashMap<String, PRDProperty> propertiesMap = new HashMap<>();

    public Properties(List<PRDProperty> list) {
        for (PRDProperty property : list)
            propertiesMap.put(property.getPRDName(), property);
    }

    public HashMap<String, PRDProperty> getPropsMap() { return propertiesMap; }

    public void setPropsMap(HashMap<String, PRDProperty> value) { propertiesMap = value; }
}
