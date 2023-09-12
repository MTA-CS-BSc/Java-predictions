package dtos.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import helpers.types.ActionTypes;

import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "actionType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IncreaseDecreaseDTO.class, name = "IncreaseDecrease"),
        @JsonSubTypes.Type(value = SetDTO.class, name = ActionTypes.SET),
        @JsonSubTypes.Type(value = ReplaceDTO.class, name = ActionTypes.REPLACE),
        @JsonSubTypes.Type(value = KillDTO.class, name = ActionTypes.KILL),
        @JsonSubTypes.Type(value = ProximityDTO.class, name = ActionTypes.PROXIMITY),
        @JsonSubTypes.Type(value = CalculationDTO.class, name = ActionTypes.CALCULATION),
        @JsonSubTypes.Type(value = SingleConditionDTO.class, name = "SingleCondition"),
        @JsonSubTypes.Type(value = MultipleConditionDTO.class, name = "MultipleCondition")
})
public class ActionDTO {
    protected String type;
    protected String entityName;
    protected SecondaryEntityDTO secondaryEntity;
    protected boolean secondaryEntityExists;

    @JsonCreator
    public ActionDTO(@JsonProperty("actionType") String type,
                     @JsonProperty("entityName") String entityName,
                     @JsonProperty("secondaryEntity") SecondaryEntityDTO secondaryEntity) {
        this.type = type;
        this.entityName = entityName;
        this.secondaryEntity = secondaryEntity;
        this.secondaryEntityExists = !Objects.isNull(secondaryEntity);
    }
    public String getType() {
        return type;
    }
    public String getEntityName() {
        return entityName;
    }
    public SecondaryEntityDTO getSecondaryEntity() {
        return secondaryEntity;
    }
    public boolean isSecondaryEntityExists() {
        return secondaryEntityExists;
    }
}
