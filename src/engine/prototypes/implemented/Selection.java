package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDAction;

public class Selection {
    protected Condition condition;
    protected String count;

    public Selection(PRDAction.PRDSecondaryEntity.PRDSelection selection) {
        condition = new Condition(selection.getPRDCondition());
        count = selection.getCount();
    }

    public Selection(Selection other) {
        count = other.getCount();
        condition = new Condition(other.getCondition());
    }

    public Condition getCondition() {
        return condition;
    }

    public String getCount() {
        return count;
    }
}
