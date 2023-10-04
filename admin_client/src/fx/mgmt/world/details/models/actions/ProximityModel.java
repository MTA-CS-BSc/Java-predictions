package fx.mgmt.world.details.models.actions;


import types.ActionTypes;

public class ProximityModel extends ActionModel {
    private final String sourceEntity;
    private final String targetEntity;
    private final String depth;
    private final int actionsAmount;

    public ProximityModel(SecondaryEntityModel secondaryEntity, String sourceEntity,
                          String targetEntity, String depth, int actionsAmount) {
        super(ActionTypes.PROXIMITY, "", secondaryEntity);

        this.actionsAmount = actionsAmount;
        this.targetEntity = targetEntity;
        this.sourceEntity = sourceEntity;
        this.depth = depth;
    }

    public String getSourceEntity() {
        return sourceEntity;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public String getDepth() {
        return depth;
    }

    public int getActionsAmount() {
        return actionsAmount;
    }

}
