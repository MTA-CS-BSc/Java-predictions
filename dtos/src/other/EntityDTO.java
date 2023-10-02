package other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import prototypes.implemented.Coordinate;

import java.util.List;

public class EntityDTO {
    protected String name;
    protected int population;
    protected List<PropertyDTO> properties;
    protected List<Coordinate> takenSpots;
    protected int initialPopulation;

    @JsonCreator
    public EntityDTO(@JsonProperty("name") String name,
                     @JsonProperty("population") int population,
                     @JsonProperty("properties") List<PropertyDTO> properties,
                     @JsonProperty("takenSpots") List<Coordinate> takenSpots,
                     @JsonProperty("initialPopulation") int initialPopulation) {
        this.name = name;
        this.properties = properties;
        this.population = population;
        this.takenSpots = takenSpots;
        this.initialPopulation = initialPopulation;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int value) {
        population = value;
    }

    public List<PropertyDTO> getProperties() {
        return properties;
    }

    public List<Coordinate> getTakenSpots() { return takenSpots; }
    @Override
    public String toString() {
        return name;
    }
}
