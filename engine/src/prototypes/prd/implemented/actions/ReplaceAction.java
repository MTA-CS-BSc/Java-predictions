package prototypes.prd.implemented.actions;

import prototypes.prd.generated.PRDAction;

public class ReplaceAction extends Action {
    protected String kill;
    protected String create;
    protected String mode;

    public ReplaceAction(PRDAction action) {
        super(action);

        kill = action.getKill();
        create = action.getCreate();
        mode = action.getMode();
    }

    public ReplaceAction(ReplaceAction other) {
        super(other);

        kill = other.getKill();
        create = other.getCreate();
        mode = other.getMode();
    }

    public String getKill() {
        return kill;
    }

    public String getCreate() {
        return create;
    }

    public String getMode() {
        return mode;
    }
}
