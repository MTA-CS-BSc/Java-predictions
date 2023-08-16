package dtos;

import java.util.List;

public class EntityDTO {
    protected String name;
    protected List<PropertyDTO> properties;

    public EntityDTO(String _name, List<PropertyDTO> _properties) {
        name = _name;
        properties = _properties;
    }
    public String getName() { return name; }
    public List<PropertyDTO> getProperties() { return properties; }
}
