package dtos;

public class SingleSimulationDTO {
    protected final String uuid;
    protected final String startTimestamp;
    protected final WorldDTO world;

    public SingleSimulationDTO(String uuid, String startTimestamp, WorldDTO world) {
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
    @Override
    public String toString() {
        return "--------------------------------------\n" +
                "----------Simulation details-----------\n" +
                "--------------------------------------\n" +
                world.toString();
    }
}
