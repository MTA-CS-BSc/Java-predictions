package dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EntityDTO {
    protected String name;
    protected int population;
    protected List<PropertyDTO> properties;

    @JsonCreator
    public EntityDTO(@JsonProperty("name") String name,
                     @JsonProperty("population") int population,
                     @JsonProperty("properties") List<PropertyDTO> properties) {
        this.name = name;
        this.properties = properties;
        this.population = population;
    }
    public String getName() { return name; }
    public List<PropertyDTO> getProperties() { return properties; }
}
