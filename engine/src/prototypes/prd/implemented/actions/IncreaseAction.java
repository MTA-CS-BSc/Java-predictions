package prototypes.prd.implemented.actions;

import prototypes.prd.generated.PRDAction;

public class IncreaseAction extends Action {
    protected String propertyName;
    protected String by;

    public IncreaseAction(PRDAction action) {
        super(action);

        propertyName = action.getProperty();
        by = action.getBy();
    }

    public IncreaseAction(IncreaseAction other) {
        super(other);
        propertyName = other.getPropertyName();
        by = other.getBy();
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getBy() {
        return by;
    }
}
