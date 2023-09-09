package dtos.actions;

import helpers.ActionTypes;

public class ProximityDTO extends ActionDTO {
    protected String sourceEntity;
    protected String targetEntity;
    protected String depth;
    protected int actionsAmount;
    public ProximityDTO(SecondaryEntityDTO secondaryEntity,
                        String sourceEntity, String targetEntity, String depth, int actionsAmount) {
        super(ActionTypes.PROXIMITY, "", secondaryEntity);
        this.sourceEntity = sourceEntity;
        this.targetEntity = targetEntity;
        this.depth = depth;
        this.actionsAmount = actionsAmount;
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
