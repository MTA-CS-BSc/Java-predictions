package engine.simulation;

import engine.prototypes.implemented.World;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

public abstract class SingleSimulationLog implements Serializable {
    protected Date start;
    protected Date finished;
    protected Date created;
    protected LinkedList<WorldState> worldStatesByTicks;

    public SingleSimulationLog() {
        worldStatesByTicks = new LinkedList<>();
    }
    public void setCreatedTime(Date created) { this.created = created; }
    public void setStartTime(Date start) {
        this.start = start;
    }
    public void setEndTime(Date finished) {
        this.finished = finished;
    }
    public Date getStartTimestamp() {
        return start;
    }
    public Date getCreatedTimestamp() { return created; }
    public Date getFinishedTimestamp() {
        return finished;
    }
    public WorldState getStartWorldState() {
        return worldStatesByTicks.getFirst();
    }
    public WorldState getLastWorldState() { return worldStatesByTicks.getLast(); }
    public void pushWorldState(WorldState worldState) {
        worldStatesByTicks.addLast(worldState);
    }
    public void pushWorldState(World world) {
        pushWorldState(new WorldState(world));
    }
    public void enqueueWorldState(WorldState worldState) { worldStatesByTicks.addFirst(worldState); }
    public void enqueueWorldState(World world) { enqueueWorldState(new WorldState(world));}
    public WorldState popWorldState() {
        return worldStatesByTicks.removeLast();
    }
}
