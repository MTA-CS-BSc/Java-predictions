package prototypes.prd.implemented;

import prototypes.SingleEntity;
import prototypes.prd.generated.PRDEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Entity implements Serializable {
    protected String name;
    protected int population;
    protected List<SingleEntity> singleEntities;
    protected Properties initialProperties;
    protected int initialPopulation;

    public Entity(PRDEntity entity) {
        name = entity.getName();
        initialPopulation = population = 0;
        singleEntities = new ArrayList<>();
        initialProperties = new Properties(entity.getPRDProperties().getPRDProperty());
    }

    public Entity(Entity other) {
        name = other.getName();
        population = other.getPopulation();
        singleEntities = new ArrayList<>();
        initialProperties = new Properties(other.getInitialProperties());

        other.getSingleEntities().forEach(singleEntity -> {
            singleEntities.add(new SingleEntity(singleEntity));
        });

        initialPopulation = other.getPopulation();
    }

    public Properties getInitialProperties() {
        return initialProperties;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public int getInitialPopulation() { return initialPopulation; }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setInitialPopulation(int population) { this.initialPopulation = population; }

    public List<SingleEntity> getSingleEntities() {
        return singleEntities;
    }

    public void setSingleEntities(List<SingleEntity> list) {
        singleEntities = list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#####Entity######\n");
        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Population: ").append(getPopulation()).append("\n");

        getInitialProperties().propertiesMap.values().forEach(property -> sb.append(property.toString()).append("\n"));

        return sb.toString();
    }
}
