package engine.prototypes.parsed;

import engine.prototypes.jaxb.PRDProperty;

import java.util.HashMap;
import java.util.List;

public class Properties {
    protected HashMap<String, PRDProperty> properties = new HashMap<>();

    public Properties(List<PRDProperty> list) {
        for (PRDProperty property : list)
            properties.put(property.getPRDName(), property);
    }

    public HashMap<String, PRDProperty> getProps() { return properties; }

    public void setProps(HashMap<String, PRDProperty> value) { properties = value; }
}
