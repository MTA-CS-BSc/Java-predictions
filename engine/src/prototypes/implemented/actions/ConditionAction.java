package prototypes.implemented.actions;

import prototypes.implemented.Condition;
import prototypes.implemented.Else;
import prototypes.implemented.Then;
import prototypes.jaxb.PRDAction;

public class ConditionAction extends Action {
    protected Condition condition;
    protected Then prdThen;
    protected Else prdElse;

    public ConditionAction(PRDAction action) {
        super(action);

        condition = new Condition(action.getPRDCondition());
        prdThen = new Then(action.getPRDThen());
        prdElse = new Else(action.getPRDElse());
    }

    public ConditionAction(ConditionAction other) {
        super(other);

        condition = new Condition(other.getCondition());
        prdThen = new Then(other.getThen());
        prdElse = new Else(other.getElse());
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
}
