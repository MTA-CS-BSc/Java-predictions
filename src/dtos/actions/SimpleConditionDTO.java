package dtos.actions;

public class SimpleConditionDTO extends ConditionDTO {
    protected String operator;
    protected String property;
    protected String value;
    public SimpleConditionDTO(String entityName, SecondaryEntityDTO secondaryEntity,
                              int thenActionsAmount, int elseActionsAmount,
                              String operator, String property, String value) {
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
