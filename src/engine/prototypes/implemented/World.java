package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDWorld;

import java.io.Serializable;

public class World implements Serializable {
    protected Environment environment;
    protected Entities entities;
    protected Termination termination;
    protected Rules rules;
    protected WorldGrid grid;

    public World(PRDWorld world) {
        rules = new Rules(world.getPRDRules().getPRDRule());
        entities = new Entities(world.getPRDEntities().getPRDEntity());
        environment = new Environment(world.getPRDEnvironment().getPRDEnvProperty());
        termination = new Termination(world.getPRDTermination());
        grid = new WorldGrid(world.getPRDGrid());
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
    @Override
    public String toString() {
        return entities.toString() + rules.toString() + termination.toString();
    }
}
