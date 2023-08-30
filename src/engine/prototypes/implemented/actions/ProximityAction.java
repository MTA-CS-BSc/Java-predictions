package engine.prototypes.implemented.actions;

import engine.prototypes.implemented.Action;
import engine.prototypes.implemented.Actions;
import engine.prototypes.implemented.Between;
import engine.prototypes.jaxb.PRDAction;

public class ProximityAction extends Action {
    protected Between between;
    protected String depthExpression;
    protected Actions actions;
    public ProximityAction(PRDAction action) {
        super(action.getType(), action.getPRDSecondaryEntity());

        depthExpression = action.getPRDEnvDepth().getOf();
        between = new Between(action.getPRDBetween());
        actions = new Actions(action.getPRDActions().getPRDAction());
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
