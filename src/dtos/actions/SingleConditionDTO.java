package dtos.actions;

public class SingleConditionDTO extends ConditionDTO {
    protected String operator;
    protected String property;
    protected String value;
    public SingleConditionDTO(String entityName, SecondaryEntityDTO secondaryEntity,
                              int thenActionsAmount, int elseActionsAmount,
                              String operator, String property, String value) {
        super(entityName, secondaryEntity, thenActionsAmount, elseActionsAmount);
        this.operator = operator;
        this.property = property;
        this.value = value;
    }
    public SingleConditionDTO(SingleConditionDTO other) {
        super(other.getEntityName(), new SecondaryEntityDTO(other.getSecondaryEntity()),
                other.getThenActionsAmount(), other.getElseActionsAmount());
        this.operator = other.getOperator();
        this.property = other.getProperty();
        this.value = other.getValue();
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
