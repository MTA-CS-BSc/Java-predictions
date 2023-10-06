package fx.component.mgmt.world.details.models.actions;

public class MultipleConditionModel extends ConditionModel {
    private final String logicalOperator;
    private final int conditionsAmount;

    public MultipleConditionModel(String entityName, SecondaryEntityModel secondaryEntity,
                                  int thenActionsAmount, int elseActionsAmount,
                                  String logicalOperator, int conditionsAmount) {
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
