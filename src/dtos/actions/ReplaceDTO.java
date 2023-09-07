package dtos.actions;

import engine.consts.ActionTypes;

public class ReplaceDTO extends ActionDTO {
    protected String kill;
    protected String create;
    protected String mode;
    public ReplaceDTO(SecondaryEntityDTO secondaryEntity,
                      String kill, String create, String mode) {
        super(ActionTypes.REPLACE, "", secondaryEntity);
        this.kill = kill;
        this.create = create;
        this.mode = mode;
    }
}
