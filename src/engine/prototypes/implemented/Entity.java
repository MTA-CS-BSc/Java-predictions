package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEntity;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    protected String name;
    protected int population;
    List<SingleEntity> singleEntities;

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

        for (int i = 0; i < population; i++)
            singleEntities.add(new SingleEntity(entity.getPRDProperties().getPRDProperty()));
    }

    public void setSingleEntities(List<SingleEntity> list) { singleEntities = list; }
}
