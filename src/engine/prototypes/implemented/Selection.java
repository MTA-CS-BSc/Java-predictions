package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDAction;

public class Selection {
    protected Condition condition;
    protected String count;

    public Selection(PRDAction.PRDSecondaryEntity.PRDSelection _selection) {
        condition = new Condition(_selection.getPRDCondition());
        count = _selection.getCount();
    }
}
