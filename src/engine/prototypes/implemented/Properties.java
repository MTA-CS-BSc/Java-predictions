package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Properties implements Serializable {
    protected Map<String, Property> propertiesMap;

    public Properties(List<PRDProperty> list) {
        propertiesMap = new HashMap<>();

        for (PRDProperty property : list)
            propertiesMap.put(property.getPRDName(), new Property(property));
    }

    public Properties(Properties other) {
        propertiesMap = new HashMap<>();

        for (Property property : other.getPropsMap().values())
            propertiesMap.put(property.getName(), new Property(property));
    }

    public Map<String, Property> getPropsMap() {
        return propertiesMap;
    }
}
