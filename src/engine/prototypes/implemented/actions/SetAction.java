package engine.prototypes.implemented.actions;

import engine.prototypes.implemented.Action;
import engine.prototypes.jaxb.PRDAction;

public class SetAction extends Action {
    protected String entityName;
    protected String propertyName;
    protected String value;
    public SetAction(PRDAction action) {
        super(action.getType(), action.getPRDSecondaryEntity());

        entityName = action.getEntity();
        propertyName = action.getProperty();
        value = action.getValue();
    }
    public String getEntityName() {
        return entityName;
    }
    public String getPropertyName() {
        return propertyName;
    }
    public String getValue() {
        return value;
    }
}
