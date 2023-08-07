package engine.simulation;

import engine.prototypes.implemented.World;
import engine.prototypes.implemented.WorldState;

import java.util.Date;
import java.util.UUID;

public class SingleSimulationLog {
    Date start;
    Date finished;
    WorldState startWorldState;
    WorldState finishWorldState;
    UUID uuid;

    public SingleSimulationLog(UUID _uuid, World _startWorldState) {
        uuid = _uuid;
        startWorldState = new WorldState(_startWorldState);
    }
    public UUID getUuid() {
        return uuid;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setFinished(Date finished) {
        this.finished = finished;
    }

    public void setFinishWorldState(World finishWorldState) {
        this.finishWorldState = new WorldState(finishWorldState);
    }

    public Date getStart() {
        return start;
    }

    public Date getFinished() {
        return finished;
    }

    public WorldState getStartWorldState() {
        return startWorldState;
    }

    public WorldState getFinishWorldState() {
        return finishWorldState;
    }
}
