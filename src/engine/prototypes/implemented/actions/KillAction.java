package engine.prototypes.implemented.actions;

import engine.prototypes.implemented.Action;
import engine.prototypes.jaxb.PRDAction;

public class KillAction extends Action {
    protected String entityName;
    public KillAction(PRDAction action) {
        super(action.getType(), action.getPRDSecondaryEntity());

        entityName = action.getEntity();
    }

    public String getEntityName() { return entityName; }
}
