package prototypes.implemented.actions;

import prototypes.jaxb.PRDAction;

public class KillAction extends Action {
    public KillAction(PRDAction action) {
        super(action);
    }

    public KillAction(KillAction other) {
        super(other);
    }
}
