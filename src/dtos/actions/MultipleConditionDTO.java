package dtos.actions;

public class MultipleConditionDTO extends ConditionDTO {
    protected String logicalOperator;
    protected int conditionsAmount;
    public MultipleConditionDTO(String entityName, SecondaryEntityDTO secondaryEntity,
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
