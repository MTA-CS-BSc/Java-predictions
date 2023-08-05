package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEnvProperty;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDRange;
import engine.prototypes.jaxb.PRDValue;

public class Property {
    protected String name;
    protected PRDRange range;
    protected PRDValue value;
    protected String type;

    public Property(Object property) {
        if (property.getClass() == PRDProperty.class) {
            name = ((PRDProperty) property).getPRDName();
            type = ((PRDProperty) property).getType();
            value = ((PRDProperty) property).getPRDValue();
            range = ((PRDProperty) property).getPRDRange();
        }

        else if (property.getClass() == PRDEnvProperty.class) {
            name = ((PRDEnvProperty) property).getPRDName();
            type = ((PRDEnvProperty) property).getType();
            range = ((PRDEnvProperty) property).getPRDRange();
            value = new PRDValue();
        }
    }
}
