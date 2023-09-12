package dtos.actions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import helpers.types.ActionTypes;

@JsonTypeName(ActionTypes.KILL)
public class KillDTO extends ActionDTO {
    @JsonCreator
    public KillDTO(String entityName, SecondaryEntityDTO secondaryEntity) {
        super(ActionTypes.KILL, entityName, secondaryEntity);
    }
}
