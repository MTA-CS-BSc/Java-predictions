package prototypes.prd.implemented.actions;

import prototypes.prd.generated.PRDAction;

public class KillAction extends Action {
    public KillAction(PRDAction action) {
        super(action);
    }

    public KillAction(KillAction other) {
        super(other);
    }
}
