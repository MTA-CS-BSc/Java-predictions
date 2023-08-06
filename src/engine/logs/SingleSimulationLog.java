package engine.logs;

import engine.prototypes.implemented.World;

import java.util.Date;
import java.util.UUID;

public class SingleSimulationLog {
    Date start;
    Date finished;
    World startWorldState;
    World finishWorldState;
    UUID uuid;

    public SingleSimulationLog(UUID _uuid, World _startWorldState) {
        uuid = _uuid;
//        startWorldState = new World(_startWorldState);
    }
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public void setStart(Date start) {
        this.start = start;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public void setStartWorldState(World startWorldState) {
        this.startWorldState = startWorldState;
    }

    public void setFinishWorldState(World finishWorldState) {
//        this.finishWorldState = new World(finishWorldState);
    }

    public Date getStart() {
        return start;
    }

    public Date getFinished() {
        return finished;
    }

    public World getStartWorldState() {
        return startWorldState;
    }

    public World getFinishWorldState() {
        return finishWorldState;
    }
}
