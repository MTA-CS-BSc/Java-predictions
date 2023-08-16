package dtos;

public class SingleSimulationDTO {
    protected final String uuid;
    protected final String startTimestamp;
    protected final WorldDTO world;

    public SingleSimulationDTO(String _uuid, String _startTimestamp, WorldDTO _world) {
        uuid = _uuid;
        startTimestamp = _startTimestamp;
        world = _world;
    }
    public SingleSimulationDTO(WorldDTO _world) {
        uuid = "";
        startTimestamp = "";
        world = _world;
    }
    public String getUuid() { return uuid; }
    public String getStartTimestamp() { return startTimestamp; }
    @Override
    public String toString() {
        return "--------------------------------------\n" +
                "----------Simulation details-----------\n" +
                "--------------------------------------\n" +
                world.toString();
    }
}
