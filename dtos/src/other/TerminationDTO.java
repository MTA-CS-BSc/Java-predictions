package other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TerminationDTO {
    protected List<StopConditionDTO> stopConditions;

    protected boolean byUser;

    @JsonCreator
    public TerminationDTO(@JsonProperty("stopConditions") List<StopConditionDTO> stopConditions,
                          @JsonProperty("byUser") boolean isByUser) {
        this.stopConditions = stopConditions;
        this.byUser = isByUser;
    }

    public boolean isByUser() {
        return byUser;
    }

    public List<StopConditionDTO> getStopConditions() {
        return stopConditions;
    }
}
