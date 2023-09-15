package dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QueueMgmtDTO {
    protected int pendingSimulations;
    protected int runningSimulations;
    protected int finishedSimulations;

    @JsonCreator
    public QueueMgmtDTO(@JsonProperty("pending") int pendingSimulations,
                        @JsonProperty("running") int runningSimulations,
                        @JsonProperty("finished") int finishedSimulations) {
        this.pendingSimulations = pendingSimulations;
        this.runningSimulations = runningSimulations;
        this.finishedSimulations = finishedSimulations;
    }

    public int getPendingSimulations() {
        return pendingSimulations;
    }

    public int getRunningSimulations() {
        return runningSimulations;
    }

    public int getFinishedSimulations() {
        return finishedSimulations;
    }
}
