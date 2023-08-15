package engine.simulation;

import engine.prototypes.implemented.World;
import engine.prototypes.implemented.WorldState;
import helpers.TypesUtils;

import java.io.Serializable;
import java.util.Date;

public abstract class SingleSimulationLog implements Serializable {
    protected String start;
    protected String finished;
    protected WorldState startWorldState;
    protected WorldState finishWorldState;
    public void setStartTime(Date start) {
        this.start = TypesUtils.formatDate(start);
    }
    public void setEndTime(Date finished) {
        this.finished = TypesUtils.formatDate(finished);
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
