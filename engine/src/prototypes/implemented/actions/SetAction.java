package prototypes.implemented.actions;

import prototypes.jaxb.PRDAction;

public class SetAction extends Action {
    protected String propertyName;
    protected String value;

    public SetAction(PRDAction action) {
        super(action);

        propertyName = action.getProperty();
        value = action.getValue();
    }

    public SetAction(SetAction other) {
        super(other);

        propertyName = other.getPropertyName();
        value = other.getValue();
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getValue() {
        return value;
    }
}
