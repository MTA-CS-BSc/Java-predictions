package engine.simulation;

import engine.prototypes.implemented.World;

public class SingleSimulationDTO {
    protected final String uuid;
    private final World world;

    public SingleSimulationDTO(SingleSimulation simulation) {
        uuid = simulation.getUUID();
        world = simulation.getWorld();
    }

    @Override
    public String toString() {
        return "--------------------------------------\n" +
                "----------Simulation details-----------\n" +
                "--------------------------------------\n" +
                world.toString();
    }
}
