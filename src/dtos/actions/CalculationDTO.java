package dtos.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import helpers.ActionTypes;

@JsonTypeName(ActionTypes.CALCULATION)
public class CalculationDTO extends ActionDTO {
    protected String operationType;
    protected String arg1;
    protected String arg2;
    @JsonCreator
    public CalculationDTO(@JsonProperty("entityName") String entityName,
                          @JsonProperty("secondaryEntity") SecondaryEntityDTO secondaryEntity,
                          @JsonProperty("operationType") String operationType,
                          @JsonProperty("arg1") String arg1,
                          @JsonProperty("arg2") String arg2) {
        super(ActionTypes.CALCULATION, entityName, secondaryEntity);
        this.operationType = operationType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
    public String getOperationType() {
        return operationType;
    }
    public String getArg1() {
        return arg1;
    }
    public String getArg2() {
        return arg2;
    }
}
