package engine.simulation;

import engine.prototypes.implemented.World;

public class SingleSimulationDTO {
    protected final String uuid;
    protected final String startTimestamp;
    private final World world;

    public SingleSimulationDTO(SingleSimulation simulation) {
        uuid = simulation.getUUID();
        startTimestamp = simulation.getStartTimestamp();
        world = simulation.getWorld();
    }

    public SingleSimulationDTO(World _world) {
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
