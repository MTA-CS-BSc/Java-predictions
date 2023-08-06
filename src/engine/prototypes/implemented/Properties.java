package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Properties {
    protected HashMap<String, Property> propertiesMap = new HashMap<>();
    public Properties(List<PRDProperty> list) {
        for (PRDProperty property : list)
            propertiesMap.put(property.getPRDName(), new Property(property));
    }

    public Properties(Properties other) {
        for (Property property : other.getPropsMap().values())
            propertiesMap.put(property.getName(), new Property(property));
    }

    public HashMap<String, Property> getPropsMap() { return propertiesMap; }
}
