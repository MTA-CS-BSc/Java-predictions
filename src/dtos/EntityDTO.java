package dtos;

import java.util.List;

public class EntityDTO {
    protected String name;
    protected int population;
    protected List<PropertyDTO> properties;

    public EntityDTO(String _name, int _population, List<PropertyDTO> _properties) {
        name = _name;
        properties = _properties;
        population = _population;
    }
    public String getName() { return name; }
    public List<PropertyDTO> getProperties() { return properties; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#####Entity######\n");
        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Population: ").append(population).append("\n");

        properties.forEach(property -> sb.append(property.toString()).append("\n"));

        return sb.toString();
    }
}
