package dtos.actions;

import helpers.ActionTypes;

public class CalculationDTO extends ActionDTO {
    protected String operationType;
    protected String arg1;
    protected String arg2;
    public CalculationDTO(String entityName, SecondaryEntityDTO secondaryEntityDTO,
                          String operationType, String arg1, String arg2) {
        super(ActionTypes.CALCULATION, entityName, secondaryEntityDTO);
        this.operationType = operationType;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
    public CalculationDTO(CalculationDTO other) {
        super(ActionTypes.CALCULATION, other.entityName, new SecondaryEntityDTO(other.getSecondaryEntity()));
        this.operationType = other.getOperationType();
        this.arg1 = other.getArg1();
        this.arg2 = other.getArg2();
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
