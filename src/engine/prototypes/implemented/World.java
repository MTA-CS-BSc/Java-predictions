package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDWorld;
import engine.simulation.WorldState;

import java.io.Serializable;
import java.util.Map;

public class World implements Serializable {
    protected Environment environment;
    protected Entities entities;
    protected Termination termination;
    protected Rules rules;
    protected WorldGrid grid;

    public World(Termination termination, Rules rules, WorldState worldState) {
        this.termination = new Termination(termination);
        this.rules = new Rules(rules);
        environment = new Environment();
        entities = new Entities();
        grid = new WorldGrid(worldState.getGrid());
        setByWorldState(worldState);
    }
    public World(PRDWorld world) {
        rules = new Rules(world.getPRDRules().getPRDRule());
        entities = new Entities(world.getPRDEntities().getPRDEntity());
        environment = new Environment(world.getPRDEnvironment().getPRDEnvProperty());
        termination = new Termination(world.getPRDTermination());
        grid = new WorldGrid(world.getPRDGrid());
    }
    public World(World other) {
        this(other.getTermination(), other.getRules(), new WorldState(other));
    }
    public Environment getEnvironment() { return environment; }
    public Entities getEntities() { return entities; }
    public Termination getTermination() { return termination; }
    public Rules getRules() { return rules; }
    public WorldGrid getGrid() { return grid; }
    public void initAllRandomVars() {
        entities.initRandomVars();
        environment.initRandomVars();
    }
    private void setEnvironmentByWorldState(Map<String, Property> envMap) {
        environment.getEnvVars().clear();

        envMap.forEach((key, value) -> {
            environment.getEnvVars().put(key, new Property(value));
        });
    }
    private void setEntitiesByWorldState(Map<String, Entity> entitiesMap) {
        entities.getEntitiesMap().clear();

        entitiesMap.forEach((key, value) -> {
            entities.getEntitiesMap().put(key, new Entity(value));
        });
    }
    private void setGridByWorldState(Map<String, Entity> entitiesMap) {
        grid.clear();

        for (Entity entity : entitiesMap.values())
            for (SingleEntity singleEntity : entity.getSingleEntities())
                grid.changeCoordinateState(singleEntity.getCoordinate());
    }
    public void setByWorldState(WorldState worldState) {
        setEnvironmentByWorldState(worldState.getEnvironmentMap());
        setEntitiesByWorldState(worldState.getEntitiesMap());
        setGridByWorldState(worldState.getEntitiesMap());
    }
    @Override
    public String toString() {
        return entities.toString() + rules.toString() + termination.toString();
    }
}
