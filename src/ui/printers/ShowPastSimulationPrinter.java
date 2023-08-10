package ui.printers;

import engine.exceptions.UUIDNotFoundException;
import engine.history.HistoryManager;
import engine.modules.Utils;

import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ShowPastSimulationPrinter {
    public static void printAvailableSimulations(HistoryManager historyManager) {
        AtomicInteger index = new AtomicInteger(1);

        historyManager.getPastSimulations().values()
                .stream()
                .sorted(Comparator.comparing(s -> s.getStartTime()))
                .forEach(simulation -> {
                    System.out.printf("%d. UUID: [%s], timestamp: [%s]",
                            index.getAndIncrement(), simulation.getUUID().toString(),
                            Utils.formatDate(simulation.getStartTime()));
                });
    }

    public static void printOutputOptions() {
        System.out.println("Please select an output option: ");
        System.out.println("1. Show entities before and after simulation");
        System.out.println("2. Show property histogram");
    }

    public static void printEntitiesBeforeAfter(HistoryManager historyManager, String simulationUUID) throws UUIDNotFoundException {
        Map<String, Integer[]> entitiesBeforeAndAfter = historyManager.getEntitiesBeforeAndAfter(simulationUUID);

        entitiesBeforeAndAfter.forEach((entityName, amount) -> {
            System.out.printf("Entity [%s]: %d->%d%n", entityName, amount[0], amount[1]);
        });
    }
}
