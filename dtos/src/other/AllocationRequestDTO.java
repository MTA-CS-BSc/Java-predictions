package other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import json.Keys;
import types.RequestState;

import java.util.List;

public class AllocationRequestDTO {
    protected String uuid;
    protected String initialWorldName;
    protected int requestedExecutions;
    protected RequestState state;
    protected String createdUser;
    protected List<SingleSimulationDTO> requestSimulations;
    protected TerminationDTO termination;

    @JsonCreator
    public AllocationRequestDTO(@JsonProperty("uuid") String uuid,
                                @JsonProperty(Keys.INITIAL_WORLD_NAME_KEY) String initialWorldName,
                                @JsonProperty("requestedExecutions") int requestedExecutions,
                                @JsonProperty("state") RequestState state,
                                @JsonProperty(Keys.CREATED_USER_KEY) String createdUser,
                                @JsonProperty("requestSimulations") List<SingleSimulationDTO> requestSimulations,
                                @JsonProperty(Keys.TERMINATION_KEY) TerminationDTO termination) {
        this.uuid = uuid;
        this.initialWorldName = initialWorldName;
        this.requestedExecutions = requestedExecutions;
        this.state = state;
        this.createdUser = createdUser;
        this.requestSimulations = requestSimulations;
        this.termination = termination;
    }

    public String getUuid() {
        return uuid;
    }

    public String getInitialWorldName() {
        return initialWorldName;
    }

    public int getRequestedExecutions() {
        return requestedExecutions;
    }

    public RequestState getState() {
        return state;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public List<SingleSimulationDTO> getRequestSimulations() {
        return requestSimulations;
    }

    public TerminationDTO getTermination() {
        return termination;
    }
}
