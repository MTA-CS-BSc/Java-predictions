package fx.models.DetailsScreen.actions;

import helpers.types.ActionTypes;

public class ReplaceModel extends ActionModel {
    private final String kill;
    private final String create;
    private final String mode;

    public ReplaceModel(String entityName, SecondaryEntityModel secondaryEntity,
                        String kill, String create, String mode) {
        super(ActionTypes.REPLACE, entityName, secondaryEntity);
        this.kill = kill;
        this.create = create;
        this.mode = mode;
    }

    public String getKill() {
        return kill;
    }

    public String getCreate() {
        return create;
    }

    public String getMode() {
        return mode;
    }
}
