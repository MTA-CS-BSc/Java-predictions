package dtos.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import helpers.ActionTypes;

@JsonTypeName(ActionTypes.SET)
public class SetDTO extends ActionDTO {
    protected String propertyName;
    protected String value;
    public SetDTO(@JsonProperty("entityName") String entityName,
                  @JsonProperty("secondaryEntity") SecondaryEntityDTO secondaryEntity,
                  @JsonProperty("propertyName") String propertyName,
                  @JsonProperty("value") String value) {
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
