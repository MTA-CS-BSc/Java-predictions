package actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("IncreaseDecrease")
public class IncreaseDecreaseDTO extends ActionDTO {
    protected String propertyName;
    protected String by;

    @JsonCreator
    public IncreaseDecreaseDTO(@JsonProperty("actionType") String type,
                               @JsonProperty("entityName") String entityName,
                               @JsonProperty("secondaryEntity") SecondaryEntityDTO secondaryEntity,
                               @JsonProperty("propertyName") String propertyName,
                               @JsonProperty("by") String by) {
        super(type, entityName, secondaryEntity);
        this.propertyName = propertyName;
        this.by = by;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getBy() {
        return by;
    }
}
