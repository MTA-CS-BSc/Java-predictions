package fx.models.DetailsScreen.actions;

import helpers.ActionTypes;

public class KillModel extends ActionModel {
    public KillModel(String entityName, SecondaryEntityModel secondaryEntity) {
        super(ActionTypes.KILL, entityName, secondaryEntity);
    }
}
