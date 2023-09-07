package dtos;

import java.util.List;

public class EntityDTO {
    protected String name;
    protected int population;
    protected List<PropertyDTO> properties;

    public EntityDTO(String name, int population, List<PropertyDTO> properties) {
        this.name = name;
        this.properties = properties;
        this.population = population;
    }
    public String getName() { return name; }
    public List<PropertyDTO> getProperties() { return properties; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#####Entity######\n");
        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Population: ").append(population).append("\n");

        sb.append("Properties: ");
        properties.forEach(property -> sb.append(property.toString()).append("\n"));

        return sb.toString();
    }
}
