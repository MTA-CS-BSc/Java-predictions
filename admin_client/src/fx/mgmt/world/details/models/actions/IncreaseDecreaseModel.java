package fx.mgmt.world.details.models.actions;

public class IncreaseDecreaseModel extends ActionModel {
    private final String propertyName;
    private final String by;

    public IncreaseDecreaseModel(String type, String entityName, SecondaryEntityModel secondaryEntity,
                                 String propertyName, String by) {
        super(type, entityName, secondaryEntity);
        this.propertyName = propertyName;
        this.by = by;
    }

    public String getBy() {
        return by;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
