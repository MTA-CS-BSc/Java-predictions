package dtos.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("SingleCondition")
public class SingleConditionDTO extends ConditionDTO {
    protected String operator;
    protected String property;
    protected String value;

    @JsonCreator
    public SingleConditionDTO(@JsonProperty("entityName") String entityName,
                              @JsonProperty("secondaryEntity") SecondaryEntityDTO secondaryEntity,
                              @JsonProperty("thenActionsAmount") int thenActionsAmount,
                              @JsonProperty("elseActionsAmount") int elseActionsAmount,
                              @JsonProperty("operator") String operator,
                              @JsonProperty("property") String property,
                              @JsonProperty("value") String value) {
        super(entityName, secondaryEntity, thenActionsAmount, elseActionsAmount);
        this.operator = operator;
        this.property = property;
        this.value = value;
    }

    public String getOperator() {
        return operator;
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }
}
