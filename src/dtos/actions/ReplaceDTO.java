package dtos.actions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import helpers.ActionTypes;

@JsonTypeName(ActionTypes.REPLACE)
public class ReplaceDTO extends ActionDTO {
    protected String kill;
    protected String create;
    protected String mode;
    public ReplaceDTO(@JsonProperty("secondaryEntity") SecondaryEntityDTO secondaryEntity,
                      @JsonProperty("kill") String kill,
                      @JsonProperty("create") String create,
                      @JsonProperty("mode") String mode) {
        super(ActionTypes.REPLACE, "", secondaryEntity);
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
