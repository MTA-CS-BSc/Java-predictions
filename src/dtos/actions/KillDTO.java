package dtos.actions;

import engine.consts.ActionTypes;

public class KillDTO extends ActionDTO {
    public KillDTO(String entityName, SecondaryEntityDTO secondaryEntity) {
        super(ActionTypes.KILL, entityName, secondaryEntity);
    }
}
