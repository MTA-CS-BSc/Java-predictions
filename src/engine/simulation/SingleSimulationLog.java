package engine.simulation;

import engine.prototypes.implemented.World;
import engine.prototypes.implemented.WorldState;

import java.util.Date;
import java.util.UUID;

abstract class SingleSimulationLog {
    protected Date start;
    protected Date finished;
    protected WorldState startWorldState;
    protected WorldState finishWorldState;
    public void setStartTime(Date start) {
        this.start = start;
    }
    public void setEndTime(Date finished) {
        this.finished = finished;
    }
    public void setStartWorldState(World startWorld) {
        this.startWorldState = new WorldState(startWorld);
    }
    public void setFinishWorldState(World finishWorld) {
        this.finishWorldState = new WorldState(finishWorld);
    }
    public Date getStartTime() {
        return start;
    }
    public Date getFinishedTime() {
        return finished;
    }
    public WorldState getStartWorldState() {
        return startWorldState;
    }
    public WorldState getFinishWorldState() {
        return finishWorldState;
    }
}
