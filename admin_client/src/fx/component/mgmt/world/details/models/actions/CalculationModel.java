package fx.component.mgmt.world.details.models.actions;


import types.ActionTypes;

public class CalculationModel extends ActionModel {
    protected String operationType;
    protected String arg1;
    protected String arg2;

    public CalculationModel(String entityName, SecondaryEntityModel secondaryEntity,
                            String operationType, String arg1, String arg2) {
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
