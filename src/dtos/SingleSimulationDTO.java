package dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleSimulationDTO {
    protected final String uuid;
    protected final String startTimestamp;
    protected final WorldDTO world;

    @JsonCreator
    public SingleSimulationDTO(@JsonProperty("uuid") String uuid,
                               @JsonProperty("startTimestamp") String startTimestamp,
                               @JsonProperty("world") WorldDTO world) {
        this.uuid = uuid;
        this.startTimestamp = startTimestamp;
        this.world = world;
    }
    public SingleSimulationDTO(WorldDTO world) {
        uuid = "";
        startTimestamp = "";
        this.world = world;
    }
    public String getUuid() { return uuid; }
    public String getStartTimestamp() { return startTimestamp; }
    public WorldDTO getWorld() {
        return world;
    }
}
