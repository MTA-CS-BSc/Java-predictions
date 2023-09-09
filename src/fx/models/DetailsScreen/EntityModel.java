package fx.models.DetailsScreen;

import java.util.List;

public class EntityModel {
    private final String name;
    private final List<PropertyModel> properties;
    public EntityModel(String name, List<PropertyModel> properties) {
        this.name = name;
        this.properties = properties;
    }
    public String getName() {
        return name;
    }
    public List<PropertyModel> getProperties() {
        return properties;
    }
}
