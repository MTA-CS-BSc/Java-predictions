package fx.models.details.actions;

import types.ActionTypes;

public class KillModel extends ActionModel {
    public KillModel(String entityName, SecondaryEntityModel secondaryEntity) {
        super(ActionTypes.KILL, entityName, secondaryEntity);
    }
}
