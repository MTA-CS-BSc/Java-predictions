package engine.prototypes.implemented;

import engine.modules.Constants;
import engine.modules.PropTypes;
import engine.modules.RandomGenerator;
import engine.prototypes.jaxb.PRDRange;
import engine.prototypes.jaxb.PRDTermination;
import engine.prototypes.jaxb.PRDWorld;

import java.util.Objects;

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
    private void setPropRandomInit(Property property, PRDRange range) {
        if (property.getType().equals(PropTypes.BOOLEAN))
            property.getValue().setInit(String.valueOf(RandomGenerator.randomizeRandomBoolean()));

        else if (property.getType().equals(PropTypes.DECIMAL))
            property.getValue().setInit(String.valueOf(RandomGenerator.randomizeRandomNumber((int)range.getFrom(), (int)range.getTo())));

        else if (property.getType().equals(PropTypes.FLOAT))
            property.getValue().setInit(String.valueOf(RandomGenerator.randomizeFloat((float)range.getFrom(), (float)range.getTo())));

        else if (property.getType().equals(PropTypes.STRING))
            property.getValue().setInit(RandomGenerator.randomizeRandomString(Constants.MAX_RANDOM_STRING_LENGTH));
    }
    public void initAllRandomVars() {
        entities.getEntitiesMap().values().forEach(entity -> {
            entity.getProperties().getPropsMap().values().forEach(property -> {
                PRDRange range = property.getRange();

                if (property.getValue().isRandomInitialize())
                    setPropRandomInit(property, range);

                property.getValue().setCurrentValue(property.getValue().getInit());
            });
        });

        environment.getEnvVars().values().forEach(property -> {
            if (!property.getValue().isRandomInitialize()
                    && Objects.isNull(property.getValue().getInit()))
                property.getValue().setRandomInitialize(true);

            PRDRange range = property.getRange();
            setPropRandomInit(property, range);
            property.getValue().setCurrentValue(property.getValue().getInit());
        });
    }
}
