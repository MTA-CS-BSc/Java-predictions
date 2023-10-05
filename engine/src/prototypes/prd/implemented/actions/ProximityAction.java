package prototypes.prd.implemented.actions;

import prototypes.prd.implemented.Actions;
import prototypes.prd.implemented.Between;
import prototypes.prd.generated.PRDAction;

public class ProximityAction extends Action {
    protected Between between;
    protected String depthExpression;
    protected Actions actions;

    public ProximityAction(PRDAction action) {
        super(action);

        depthExpression = action.getPRDEnvDepth().getOf();
        between = new Between(action.getPRDBetween());
        actions = new Actions(action.getPRDActions().getPRDAction());
    }

    public ProximityAction(ProximityAction other) {
        super(other);

        depthExpression = other.getDepthExpression();
        actions = new Actions(other.getActions());
        between = new Between(other.getBetween());
    }

    public Between getBetween() {
        return between;
    }

    public String getDepthExpression() {
        return depthExpression;
    }

    public Actions getActions() {
        return actions;
    }
}
