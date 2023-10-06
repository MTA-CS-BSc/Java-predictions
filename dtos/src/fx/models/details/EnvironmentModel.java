package fx.models.details;

import com.sun.xml.internal.ws.util.StringUtils;
import fx.models.TreeItemModel;

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
