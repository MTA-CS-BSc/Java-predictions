package dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.types.SimulationState;

public class SingleSimulationDTO {
    protected final String uuid;
    protected final String createdTimestamp;
    protected final WorldDTO world;
    protected final SimulationState simulationState;
    protected final long ticks;
    protected final long elapsedTimeMillis;

    @JsonCreator
    public SingleSimulationDTO(@JsonProperty("uuid") String uuid,
                               @JsonProperty("createdTimestamp") String createdTimestamp,
                               @JsonProperty("world") WorldDTO world,
                               @JsonProperty("state") SimulationState state,
                               @JsonProperty("ticks") long ticks,
                               @JsonProperty("elapsedTime") long elapsedTimeMillis) {
        this.uuid = uuid;
        this.createdTimestamp = createdTimestamp;
        this.world = world;
        this.simulationState = state;
        this.ticks = ticks;
        this.elapsedTimeMillis = elapsedTimeMillis;
    }
    public SingleSimulationDTO(WorldDTO world) {
        uuid = "";
        createdTimestamp = "";
        this.world = world;
        this.simulationState = SimulationState.CREATED;
        this.ticks = 0;
        this.elapsedTimeMillis = 0;
    }
    public String getUuid() { return uuid; }
    public String getCreatedTimestamp() { return createdTimestamp; }
    public WorldDTO getWorld() {
        return world;
    }
    public SimulationState getSimulationState() { return simulationState; }
    public long getTicks() { return ticks; }
    public long getElapsedTimeMillis() { return elapsedTimeMillis; }
}
