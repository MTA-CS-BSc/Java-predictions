package engine.simulation;

import engine.modules.Utils;
import engine.prototypes.implemented.World;
import engine.prototypes.implemented.WorldState;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

abstract class SingleSimulationLog implements Serializable {
    protected String start;
    protected String finished;
    protected WorldState startWorldState;
    protected WorldState finishWorldState;
    public void setStartTime(Date start) {
        this.start = Utils.formatDate(start);
    }
    public void setEndTime(Date finished) {
        this.finished = Utils.formatDate(finished);
    }
    public void setStartWorldState(World startWorld) {
        this.startWorldState = new WorldState(startWorld);
    }
    public void setFinishWorldState(World finishWorld) {
        this.finishWorldState = new WorldState(finishWorld);
    }
    public String getStartTimestamp() {
        return start;
    }
    public String getFinishedTimestamp() {
        return finished;
    }
    public WorldState getStartWorldState() {
        return startWorldState;
    }
    public WorldState getFinishWorldState() {
        return finishWorldState;
    }
}
