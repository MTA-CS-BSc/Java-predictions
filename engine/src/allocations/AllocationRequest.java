package allocations;

import other.TerminationDTO;
import simulation.SingleSimulation;
import types.RequestState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AllocationRequest {
    protected String uuid;
    protected String initialWorldName;
    protected int requestedExecutions;
    protected RequestState state;
    protected String createdUser;
    protected Map<String, SingleSimulation> requestSimulations;
    protected TerminationDTO termination;

    public AllocationRequest(String initialWorldName, int requestedExecutions,
                             String createdUser, TerminationDTO termination) {
        uuid = UUID.randomUUID().toString();
        state = RequestState.CREATED;
        requestSimulations = new HashMap<>();
        this.initialWorldName = initialWorldName;
        this.requestedExecutions = requestedExecutions;
        this.createdUser = createdUser;
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

    public TerminationDTO getTermination() {
        return termination;
    }

    public Map<String, SingleSimulation> getRequestSimulations() {
        return requestSimulations;
    }

    public int getUsedSimulationsAmount() {
        return requestSimulations.size();
    }

    public boolean canExecute() {
        return requestedExecutions > requestSimulations.size();
    }
}
