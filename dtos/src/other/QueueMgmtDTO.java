package other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QueueMgmtDTO {
    protected int pending;
    protected int running;
    protected int finished;

    @JsonCreator
    public QueueMgmtDTO(@JsonProperty("pending") int pending,
                        @JsonProperty("running") int running,
                        @JsonProperty("finished") int finished) {
        this.pending = pending;
        this.running = running;
        this.finished = finished;
    }

    public int getPending() {
        return pending;
    }

    public int getRunning() {
        return running;
    }

    public int getFinished() {
        return finished;
    }
}
