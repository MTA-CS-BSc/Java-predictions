package engine.prototypes;

import engine.prototypes.implemented.Entity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityDTO {
    protected String name;
    protected List<PropertyDTO> properties;

    public EntityDTO(Entity entity) {
        name = entity.getName();
        properties = entity.getInitialProperties().getPropsMap().values()
                .stream()
                .map(PropertyDTO::new)
                .sorted(Comparator.comparing(PropertyDTO::getName))
                .collect(Collectors.toList());
    }
    public String getName() { return name; }
    public List<PropertyDTO> getProperties() { return properties; }
}
