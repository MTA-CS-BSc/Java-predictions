package fx.models.DetailsScreen.actions;

import helpers.ActionTypes;

public class SetModel extends ActionModel {
    private final String propertyName;
    private final String value;
    public SetModel(String entityName, SecondaryEntityModel secondaryEntity,
                    String propertyName, String value) {
        super(ActionTypes.SET, entityName, secondaryEntity);
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getPropertyName() { return propertyName; }
    public String getValue() { return value; }
}
