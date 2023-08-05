package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDTermination;
import engine.prototypes.jaxb.PRDWorld;

public class World {
    protected Environment environment;
    protected Entities entities;
    protected PRDTermination termination;
    protected Rules rules;

    public World(PRDWorld world) {
        rules = new Rules(world.getPRDRules().getPRDRule());
        entities = new Entities(world.getPRDEntities().getPRDEntity());
        environment = new Environment(world.getPRDEvironment().getPRDEnvProperty());
        termination = world.getPRDTermination();
    }

    public Environment getEnvironment() { return environment; }
    public Entities getEntities() { return entities; }
    public PRDTermination getTermination() { return termination; }

    public Rules getRules() { return rules; }
}
