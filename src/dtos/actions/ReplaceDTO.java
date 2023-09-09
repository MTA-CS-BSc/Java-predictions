package dtos.actions;

import helpers.ActionTypes;

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
    public ReplaceDTO(ReplaceDTO other) {
        super(ActionTypes.REPLACE, other.getEntityName(), new SecondaryEntityDTO(other.getSecondaryEntity()));
        this.kill = other.getKill();
        this.create = other.getCreate();
        this.mode = other.getMode();
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
