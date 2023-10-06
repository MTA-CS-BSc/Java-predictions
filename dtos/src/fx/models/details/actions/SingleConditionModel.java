package fx.models.details.actions;

public class SingleConditionModel extends ConditionModel {
    private final String operator;
    private final String property;
    private final String value;

    public SingleConditionModel(String entityName, SecondaryEntityModel secondaryEntity,
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
