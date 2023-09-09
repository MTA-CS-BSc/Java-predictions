package dtos.actions;

import helpers.ActionTypes;

public abstract class ConditionDTO extends ActionDTO {
    protected int thenActionsAmount;
    protected int elseActionsAmount;

    public ConditionDTO(String entityName, SecondaryEntityDTO secondaryEntity,
                     int thenActionsAmount, int elseActionsAmount) {
        super(ActionTypes.CONDITION, entityName, secondaryEntity);
        this.thenActionsAmount = thenActionsAmount;
        this.elseActionsAmount = elseActionsAmount;
    }
    public int getThenActionsAmount() {
        return thenActionsAmount;
    }
    public int getElseActionsAmount() {
        return elseActionsAmount;
    }
}
