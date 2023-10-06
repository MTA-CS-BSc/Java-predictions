package fx.component.mgmt.world.details.models;

public class EntityPropertyModel extends PropertyModel {
    private final String value;

    public EntityPropertyModel(String name, String type, RangeModel range, String value) {
        super(name, type, range);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
