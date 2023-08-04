package engine.prototypes.parsed;

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
    }
}
