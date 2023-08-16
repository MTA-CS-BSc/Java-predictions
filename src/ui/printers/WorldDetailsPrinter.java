package ui.printers;

import dtos.SingleSimulationDTO;

public class WorldDetailsPrinter {
    public static void print(SingleSimulationDTO singleSimulation) {
        System.out.println(singleSimulation.toString());
    }
}
