package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDProperty;

import java.io.Serializable;
import java.util.List;

public class SingleEntity implements Serializable {
    protected Properties properties;

    public SingleEntity(List<PRDProperty> props) {
        properties = new Properties(props);
    }

    public SingleEntity(SingleEntity other) {
        properties = new Properties(other.getProperties());
    }
    public Properties getProperties() {
        return properties;
    }
}
