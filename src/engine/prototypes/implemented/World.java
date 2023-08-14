package engine.prototypes.implemented;

import engine.consts.Restrictions;
import engine.consts.PropTypes;
import engine.modules.RandomGenerator;
import engine.modules.Utils;
import engine.prototypes.jaxb.PRDRange;
import engine.prototypes.jaxb.PRDTermination;
import engine.prototypes.jaxb.PRDWorld;

import java.io.Serializable;
import java.util.Objects;

public class World implements Serializable {
    protected Environment environment;
    protected Entities entities;
    protected Termination termination;
    protected Rules rules;

    public World(PRDWorld world) {
        rules = new Rules(world.getPRDRules().getPRDRule());
        entities = new Entities(world.getPRDEntities().getPRDEntity());
        environment = new Environment(world.getPRDEvironment().getPRDEnvProperty());
        termination = new Termination(world.getPRDTermination());
    }
    public Environment getEnvironment() { return environment; }
    public Entities getEntities() { return entities; }
    public Termination getTermination() { return termination; }
    public Rules getRules() { return rules; }
    public void initAllRandomVars() {
        entities.getEntitiesMap().values().forEach(entity -> {
            entity.getSingleEntities().forEach(singleEntity -> {
                singleEntity.getProperties().getPropsMap().values().forEach(property -> {
                    Range range = property.getRange();

                    if (property.getValue().isRandomInitialize())
                        Utils.setPropRandomInit(property, range);

                    else if (property.getValue().getInit().isEmpty())
                        Utils.setPropRandomInit(property, range);

                    property.getValue().setCurrentValue(property.getValue().getInit());
                });
            });
        });

        environment.getEnvVars().values().forEach(property -> {
            if (!property.getValue().isRandomInitialize()
                    && Objects.isNull(property.getValue().getInit()))
                property.getValue().setRandomInitialize(true);

            Range range = property.getRange();
            Utils.setPropRandomInit(property, range);
            property.getValue().setCurrentValue(property.getValue().getInit());
        });
    }
    @Override
    public String toString() {
        return entities.toString() + rules.toString() + termination.toString();
    }
}
