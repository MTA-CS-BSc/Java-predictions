package other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import types.ByStep;
import types.SimulationState;

public class SingleSimulationDTO {
    protected final String uuid;
    protected final String createdTimestamp;
    protected final WorldDTO world;
    protected final SimulationState simulationState;
    protected final long ticks;
    protected final long elapsedTimeMillis;
    protected final ByStep byStep;
    protected final String createdUser;
    protected final String requestUuid;

    @JsonCreator
    public SingleSimulationDTO(@JsonProperty("uuid") String uuid,
                               @JsonProperty("createdTimestamp") String createdTimestamp,
                               @JsonProperty("world") WorldDTO world,
                               @JsonProperty("state") SimulationState state,
                               @JsonProperty("ticks") long ticks,
                               @JsonProperty("elapsedTime") long elapsedTimeMillis,
                               @JsonProperty("byStep") ByStep byStep,
                               @JsonProperty("createdUser") String createdUser,
                               @JsonProperty("requestUuid") String requestUuid) {
        this.uuid = uuid;
        this.createdTimestamp = createdTimestamp;
        this.world = world;
        this.simulationState = state;
        this.ticks = ticks;
        this.elapsedTimeMillis = elapsedTimeMillis;
        this.byStep = byStep;
        this.createdUser = createdUser;
        this.requestUuid = requestUuid;
    }

    public SingleSimulationDTO(String requestUuid, String createdUser, WorldDTO world) {
        uuid = "";
        createdTimestamp = "";
        this.createdUser = createdUser;
        this.requestUuid = requestUuid;
        this.world = world;
        this.simulationState = SimulationState.CREATED;
        this.ticks = 0;
        this.elapsedTimeMillis = 0;
        this.byStep = ByStep.NOT_BY_STEP;
    }

    public String getUuid() {
        return uuid;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public WorldDTO getWorld() {
        return world;
    }

    public SimulationState getSimulationState() {
        return simulationState;
    }

    public long getTicks() {
        return ticks;
    }

    public long getElapsedTimeMillis() {
        return elapsedTimeMillis;
    }

    public ByStep getByStep() { return byStep; }

    public String getCreatedUser() {
        return createdUser;
    }

    public String getRequestUuid() { return requestUuid; }
}
