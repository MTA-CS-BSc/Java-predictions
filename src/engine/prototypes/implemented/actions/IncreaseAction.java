package engine.prototypes.implemented.actions;

import engine.prototypes.jaxb.PRDAction;

public class IncreaseAction extends Action {
    protected String entityName;

    protected String propertyName;

    protected String by;
    public IncreaseAction(PRDAction action) {
        super(action.getType(), action.getPRDSecondaryEntity());

        entityName = action.getEntity();
        propertyName = action.getProperty();
        by = action.getBy();
    }
    public String getEntityName() {
        return entityName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getBy() {
        return by;
    }
}
