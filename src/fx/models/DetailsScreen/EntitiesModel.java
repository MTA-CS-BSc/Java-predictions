package fx.models.DetailsScreen;

import com.sun.xml.internal.ws.util.StringUtils;
import fx.models.WorldTreeViewCategories;

import java.util.List;

public class EntitiesModel extends TreeItemModel {
    private final List<EntityModel> entities;

    public EntitiesModel(List<EntityModel> entities) {
        super(StringUtils.capitalize(WorldTreeViewCategories.ENTITIES.name().toLowerCase()));
        this.entities = entities;
    }
    public List<EntityModel> getEntities() {
        return entities;
    }
}
