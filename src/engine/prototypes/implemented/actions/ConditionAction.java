package engine.prototypes.implemented.actions;

import engine.prototypes.implemented.Condition;
import engine.prototypes.implemented.Else;
import engine.prototypes.implemented.Then;
import engine.prototypes.jaxb.PRDAction;

public class ConditionAction extends Action {
    protected String entityName;
    protected Condition condition;
    protected Then prdThen;
    protected Else prdElse;

    public ConditionAction(PRDAction action) {
        super(action.getType(), action.getPRDSecondaryEntity());

        entityName = action.getEntity();
        condition = new Condition(action.getPRDCondition());
        prdThen = new Then(action.getPRDThen());
        prdElse = new Else(action.getPRDElse());
    }
    public Condition getCondition() {
        return condition;
    }

    public Then getThen() {
        return prdThen;
    }

    public Else getElse() {
        return prdElse;
    }
    public String getEntityName() { return entityName; }
}
