package dtos.actions;

import helpers.ActionTypes;

public class SetDTO extends ActionDTO {
    protected String propertyName;
    protected String value;
    public SetDTO(String entityName, SecondaryEntityDTO secondaryEntity,
                  String propertyName, String value) {
        super(ActionTypes.SET, entityName, secondaryEntity);
        this.propertyName = propertyName;
        this.value = value;
    }
    public SetDTO(SetDTO other) {
        super(ActionTypes.SET, other.getEntityName(), new SecondaryEntityDTO(other.getSecondaryEntity()));
        this.propertyName = other.getPropertyName();
        this.value = other.getValue();
    }
    public String getPropertyName() {
        return propertyName;
    }
    public String getValue() {
        return value;
    }
}
