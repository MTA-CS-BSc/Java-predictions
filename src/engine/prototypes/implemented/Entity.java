package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEntity;

public class Entity {
    protected int population;
    protected String name;
    protected Properties properties;

    public Entity(PRDEntity entity) {
        name = entity.getName();
        population = entity.getPRDPopulation();
        properties = new Properties(entity.getPRDProperties().getPRDProperty());
    }
}
