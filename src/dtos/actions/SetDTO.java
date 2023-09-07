package dtos.actions;

import engine.consts.ActionTypes;

public class SetDTO extends ActionDTO {
    protected String propertyName;
    protected String value;
    public SetDTO(String entityName, SecondaryEntityDTO secondaryEntity,
                  String propertyName, String value) {
        super(ActionTypes.SET, entityName, secondaryEntity);
        this.propertyName = propertyName;
        this.value = value;
    }
    public String getPropertyName() {
        return propertyName;
    }
    public String getValue() {
        return value;
    }
}
