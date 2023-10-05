package other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TerminationDTO {
    protected List<StopConditionDTO> stopConditions;

    protected boolean byUser;

    @JsonCreator
    public TerminationDTO(@JsonProperty("stopConditions") Set<StopConditionDTO> stopConditions,
                          @JsonProperty("byUser") boolean byUser) {
        this.stopConditions = new ArrayList<>(stopConditions);
        this.byUser = byUser;
    }

    public boolean isByUser() {
        return byUser;
    }

    public List<StopConditionDTO> getStopConditions() {
        return stopConditions;
    }

    @Override
    public String toString() {
        if (byUser)
            return "By User";

        StringBuilder stopConditionsString = new StringBuilder();

        stopConditions.forEach(stopCondition -> {
           stopConditionsString.append(stopCondition.getCount()).append(" ").append(stopCondition.getByWho()).append("; ");
        });

        return stopConditionsString.toString();
    }
}
