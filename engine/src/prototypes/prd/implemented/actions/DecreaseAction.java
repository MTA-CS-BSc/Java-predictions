package prototypes.prd.implemented.actions;

import prototypes.prd.generated.PRDAction;

public class DecreaseAction extends Action {
    protected String propertyName;
    protected String by;

    public DecreaseAction(PRDAction action) {
        super(action);

        propertyName = action.getProperty();
        by = action.getBy();
    }

    public DecreaseAction(DecreaseAction other) {
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
