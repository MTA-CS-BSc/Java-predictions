package dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.types.SimulationState;

public class SingleSimulationDTO {
    protected final String uuid;
    protected final String startTimestamp;
    protected final WorldDTO world;
    protected final SimulationState simulationState;
    protected final long ticks;

    @JsonCreator
    public SingleSimulationDTO(@JsonProperty("uuid") String uuid,
                               @JsonProperty("startTimestamp") String startTimestamp,
                               @JsonProperty("world") WorldDTO world,
                               @JsonProperty("state") SimulationState state,
                               @JsonProperty("ticks") long ticks) {
        this.uuid = uuid;
        this.startTimestamp = startTimestamp;
        this.world = world;
        this.simulationState = state;
        this.ticks = ticks;
    }
    public SingleSimulationDTO(WorldDTO world) {
        uuid = "";
        startTimestamp = "";
        this.world = world;
        this.simulationState = SimulationState.CREATED;
        this.ticks = 0;
    }
    public String getUuid() { return uuid; }
    public String getStartTimestamp() { return startTimestamp; }
    public WorldDTO getWorld() {
        return world;
    }
    public SimulationState getSimulationState() { return simulationState; }
    public long getTicks() { return ticks; }
}
