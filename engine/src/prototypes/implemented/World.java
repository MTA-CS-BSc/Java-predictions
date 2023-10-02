package prototypes.implemented;

import prototypes.jaxb.PRDWorld;
import simulation.WorldState;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class World implements Serializable {
    protected Environment environment;
    protected Entities entities;
    protected Termination termination;
    protected Rules rules;
    protected WorldGrid grid;
    protected String name;
    protected int sleep;

    public World(World other, WorldState worldState) {
        this.termination = new Termination(other.getTermination());
        this.rules = new Rules(other.getRules());
        this.name = other.name;
        this.sleep = other.sleep;

        environment = new Environment();
        entities = new Entities();
        grid = new WorldGrid(worldState.getGrid());

        setByWorldState(worldState);
    }

    public World(PRDWorld world) {
        rules = new Rules(world.getPRDRules().getPRDRule());
        entities = new Entities(world.getPRDEntities().getPRDEntity());
        environment = new Environment(world.getPRDEnvironment().getPRDEnvProperty());
        termination = new Termination();
        grid = new WorldGrid(world.getPRDGrid());
        name = world.getName();

        Integer otherSleep = world.getSleep();
        sleep = !Objects.isNull(otherSleep) ? otherSleep : 0;
    }

    public World(World other) {
        this(other, new WorldState(other));
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Entities getEntities() {
        return entities;
    }

    public Termination getTermination() {
        return termination;
    }

    public Rules getRules() {
        return rules;
    }

    public WorldGrid getGrid() {
        return grid;
    }

    public String getName() { return name; }

    public int getSleep() { return sleep; }

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

    public void addStopCondition(Object stopCondition) {
        termination.addStopCondition(stopCondition);
    }

    public void setTerminationByUser() {
        termination.setStopByUser(true);
    }

    @Override
    public String toString() {
        return entities.toString() + rules.toString() + termination.toString();
    }
}
