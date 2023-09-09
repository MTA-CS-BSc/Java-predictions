package fx.models.DetailsScreen.actions;

import helpers.ActionTypes;

public class ConditionModel extends ActionModel {
    private final int thenActionsAmount;
    private final int elseActionsAmount;
    public ConditionModel(String entityName, SecondaryEntityModel secondaryEntity,
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
