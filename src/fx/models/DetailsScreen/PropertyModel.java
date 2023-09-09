package fx.models.DetailsScreen;

import java.util.Objects;

public class PropertyModel extends TreeItemModel {
    private final String type;
    private final RangeModel range;
    public PropertyModel(String name, String type, RangeModel range) {
        super(name);
        this.type = type;
        this.range = range;
    }
    public String getType() {
        return type;
    }
    public RangeModel getRange() {
        return range;
    }
    public boolean hasNoRange() { return Objects.isNull(range); }
}
