package actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import types.ActionTypes;

@JsonTypeName(ActionTypes.PROXIMITY)
public class ProximityDTO extends ActionDTO {
    protected String sourceEntity;
    protected String targetEntity;
    protected String depth;
    protected int actionsAmount;

    public ProximityDTO(@JsonProperty("secondaryEntity") SecondaryEntityDTO secondaryEntity,
                        @JsonProperty("sourceEntity") String sourceEntity,
                        @JsonProperty("targetEntity") String targetEntity,
                        @JsonProperty("depth") String depth,
                        @JsonProperty("actionsAmount") int actionsAmount) {
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
