package fx.models.DetailsScreen.actions;

import helpers.types.ActionTypes;

public class KillModel extends ActionModel {
    public KillModel(String entityName, SecondaryEntityModel secondaryEntity) {
        super(ActionTypes.KILL, entityName, secondaryEntity);
    }
}
