package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Entity implements Serializable {
    protected String name;
    protected int population;
    protected List<SingleEntity> singleEntities;
    protected Properties initialProperties;
    public Entity(PRDEntity entity) {
        name = entity.getName();
        population = 0;
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
    public void setPopulation(int population) {
        //TODO: Check if population exceeds current taken spots
        this.population = population;
    }
    public List<SingleEntity> getSingleEntities() {
        return singleEntities;
    }
    public void setSingleEntities(List<SingleEntity> list) { singleEntities = list; }
    public void initPopulation(int _population) {
        //TODO: Add usage
        setPopulation(_population);

        for (int i = 0; i < population; i++)
            singleEntities.add(new SingleEntity(name, getInitialProperties()));
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
