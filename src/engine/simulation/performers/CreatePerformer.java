package engine.simulation.performers;

import engine.prototypes.implemented.*;
import helpers.Constants;
import helpers.types.PropTypes;

import java.util.Objects;

public class CreatePerformer {
    public static SingleEntity scratch(Entity entity, WorldGrid grid) {
        return new SingleEntity(entity.getName(), entity.getInitialProperties(), grid);
    }

    public static SingleEntity derived(Entity create, Entity from, SingleEntity singleFrom, WorldGrid grid) {
        SingleEntity created = scratch(create, grid);

        created.getProperties().getPropsMap().values()
                .stream()
                .filter(property -> from.getInitialProperties().getPropsMap().containsKey(property.getName()))
                .forEach(property -> {
                    Property fromProperty = Objects.isNull(singleFrom) ?
                            from.getInitialProperties().getPropsMap().get(property.getName())
                            : singleFrom.getProperties().getPropsMap().get(property.getName());

                    if (!fromProperty.hasNoRange())
                        property.setRange(new Range(fromProperty.getRange()));

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
