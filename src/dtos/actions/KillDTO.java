package dtos.actions;

import helpers.ActionTypes;

public class KillDTO extends ActionDTO {
    public KillDTO(String entityName, SecondaryEntityDTO secondaryEntity) {
        super(ActionTypes.KILL, entityName, secondaryEntity);
    }
    public KillDTO(KillDTO other) {
        super(ActionTypes.KILL, other.getEntityName(), new SecondaryEntityDTO(other.getSecondaryEntity()));
    }
}
