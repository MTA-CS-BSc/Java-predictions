package ui.printers;

import engine.simulation.SingleSimulationDTO;

public class WorldDetailsPrinter {
    public static void print(SingleSimulationDTO singleSimulation) {
        System.out.println(singleSimulation.toString());
    }
}
