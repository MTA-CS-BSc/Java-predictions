package engine.simulation.performers;

import engine.consts.PropTypes;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.Value;
import helpers.Constants;

import java.util.Objects;

public class CreatePerformer {
    public static SingleEntity scratch(Entity entity) {
        SingleEntity created = new SingleEntity(entity.getInitialProperties());
        entity.getSingleEntities().add(created);
        entity.setPopulation(entity.getPopulation() + 1);

        return created;
    }
    public static SingleEntity derived(Entity create, Entity from, SingleEntity singleFrom) {
        SingleEntity created = scratch(create);

        created.getProperties().getPropsMap().values()
                .stream()
                .filter(property -> from.getInitialProperties().getPropsMap().containsKey(property.getName()))
                .forEach(property -> {
                    Property fromProperty = Objects.isNull(singleFrom) ?
                            from.getInitialProperties().getPropsMap().get(property.getName())
                            : singleFrom.getProperties().getPropsMap().get(property.getName());

                    if (property.getType().equals(fromProperty.getType())
                            || (property.getType().equals(PropTypes.FLOAT) && fromProperty.getType().equals(PropTypes.DECIMAL)))
                       property.setValue(new Value(fromProperty.getValue()));

                    else if (property.getType().equals(PropTypes.DECIMAL) && fromProperty.getType().equals(PropTypes.FLOAT))
                        if (!fromProperty.getValue().getCurrentValue().contains("\\.")
                            || fromProperty.getValue().getCurrentValue().matches(Constants.REGEX_ONLY_ZEROES_AFTER_DOT))
                            property.setValue(new Value(fromProperty.getValue()));
                });

        return created;
    }
}
