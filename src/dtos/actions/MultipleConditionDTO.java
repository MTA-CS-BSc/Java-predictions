package dtos.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("MultipleCondition")
public class MultipleConditionDTO extends ConditionDTO {
    protected String logicalOperator;
    protected int conditionsAmount;
    @JsonCreator
    public MultipleConditionDTO(@JsonProperty("entityName") String entityName,
                                @JsonProperty("secondaryEntity") SecondaryEntityDTO secondaryEntity,
                                @JsonProperty("thenActionsAmount") int thenActionsAmount,
                                @JsonProperty("elseActionsAmount") int elseActionsAmount,
                                @JsonProperty("logicalOperator") String logicalOperator,
                                @JsonProperty("conditionsAmount") int conditionsAmount) {
        super(entityName, secondaryEntity, thenActionsAmount, elseActionsAmount);
        this.logicalOperator = logicalOperator;
        this.conditionsAmount = conditionsAmount;
    }
    public String getLogicalOperator() {
        return logicalOperator;
    }
    public int getConditionsAmount() {
        return conditionsAmount;
    }
}
