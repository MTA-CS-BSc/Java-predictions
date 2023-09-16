package fx.models.DetailsScreen;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.List;

public class EnvironmentModel extends TreeItemModel {
    private final List<PropertyModel> properties;

    public EnvironmentModel(List<PropertyModel> properties) {
        super(StringUtils.capitalize(WorldTreeViewCategories.ENVIRONMENT.name().toLowerCase()));
        this.properties = properties;
    }

    public List<PropertyModel> getProperties() {
        return properties;
    }
}
