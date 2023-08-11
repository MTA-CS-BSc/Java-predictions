package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Entity implements Serializable {
    protected String name;
    protected int population;
    List<SingleEntity> singleEntities;
    Properties initialProperties;

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
        this.population = population;
    }
    public List<SingleEntity> getSingleEntities() {
        return singleEntities;
    }
    public Entity(PRDEntity entity) {
        name = entity.getName();
        population = entity.getPRDPopulation();
        singleEntities = new ArrayList<>();
        initialProperties = new Properties(entity.getPRDProperties().getPRDProperty());

        for (int i = 0; i < population; i++)
            singleEntities.add(new SingleEntity(entity.getPRDProperties().getPRDProperty()));
    }

    public Entity(Entity other) {
        name = other.getName();
        population = other.getPopulation();
        singleEntities = new ArrayList<>();

        other.getSingleEntities().forEach(singleEntity -> {
           singleEntities.add(new SingleEntity(singleEntity));
        });
    }

    public void setSingleEntities(List<SingleEntity> list) { singleEntities = list; }
}
