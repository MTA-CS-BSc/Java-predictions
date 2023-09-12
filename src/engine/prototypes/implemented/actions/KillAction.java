package engine.prototypes.implemented.actions;

import engine.prototypes.jaxb.PRDAction;

public class KillAction extends Action {
    public KillAction(PRDAction action) {
        super(action);
    }

    public KillAction(KillAction other) {
        super(other);
    }
}
