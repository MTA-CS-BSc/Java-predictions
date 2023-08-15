package engine.prototypes;

import engine.prototypes.implemented.Property;

public class PropertyDTO {
    protected String name;
    protected String type;

    public PropertyDTO(Property property) {
        name = property.getName();
        type = property.getType();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
