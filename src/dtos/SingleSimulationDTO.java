package dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.types.SimulationState;

public class SingleSimulationDTO {
    protected final String uuid;
    protected final String startTimestamp;
    protected final WorldDTO world;
    protected final SimulationState simulationState;

    @JsonCreator
    public SingleSimulationDTO(@JsonProperty("uuid") String uuid,
                               @JsonProperty("startTimestamp") String startTimestamp,
                               @JsonProperty("world") WorldDTO world,
                               @JsonProperty("state") SimulationState state) {
        this.uuid = uuid;
        this.startTimestamp = startTimestamp;
        this.world = world;
        this.simulationState = state;
    }
    public SingleSimulationDTO(WorldDTO world) {
        uuid = "";
        startTimestamp = "";
        this.world = world;
        this.simulationState = SimulationState.CREATED;
    }
    public String getUuid() { return uuid; }
    public String getStartTimestamp() { return startTimestamp; }
    public WorldDTO getWorld() {
        return world;
    }
    public SimulationState getSimulationState() { return simulationState; }
}
