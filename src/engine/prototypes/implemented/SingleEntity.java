package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDProperty;

import java.util.List;

public class SingleEntity {
    protected Properties properties;

    public SingleEntity(List<PRDProperty> props) {
        properties = new Properties(props);
    }
    public Properties getProperties() {
        return properties;
    }
}
