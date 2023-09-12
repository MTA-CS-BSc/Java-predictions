package engine.prototypes.implemented.actions;

import engine.prototypes.implemented.Actions;
import engine.prototypes.implemented.Between;
import engine.prototypes.jaxb.PRDAction;

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
