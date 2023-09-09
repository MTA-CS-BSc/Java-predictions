package fx.models.DetailsScreen;

import java.util.List;

public class EntityModel extends TreeItemModel {
    private final List<EntityPropertyModel> properties;
    public EntityModel(String name, List<EntityPropertyModel> properties) {
        super(name);
        this.properties = properties;
    }
    public List<EntityPropertyModel> getProperties() {
        return properties;
    }
}
