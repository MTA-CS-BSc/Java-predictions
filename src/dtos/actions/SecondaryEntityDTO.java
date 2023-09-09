package dtos.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SecondaryEntityDTO {
    protected String entityName;
    @JsonCreator
    public SecondaryEntityDTO(@JsonProperty("entityName") String entityName) {
        this.entityName = entityName;
    }
    public String getEntityName() { return entityName; }
}
