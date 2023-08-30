package engine.prototypes.implemented.actions;

import engine.prototypes.jaxb.PRDAction;

public class SetAction extends Action {
    protected String propertyName;
    protected String value;
    public SetAction(PRDAction action) {
        super(action);

        propertyName = action.getProperty();
        value = action.getValue();
    }
    public String getPropertyName() {
        return propertyName;
    }
    public String getValue() {
        return value;
    }
}
