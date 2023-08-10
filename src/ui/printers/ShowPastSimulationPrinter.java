package ui.printers;

import engine.exceptions.UUIDNotFoundException;
import engine.history.HistoryManager;
import engine.modules.Utils;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.simulation.SingleSimulation;

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

    public static void propHistogramPrintEntities(SingleSimulation simulation) {
        AtomicInteger index = new AtomicInteger(1);

        simulation.getStartWorldState().getEntitiesMap()
                .values()
                .stream()
                .sorted(Comparator.comparing(Entity::getName))
                .forEach(entity -> {
                    System.out.printf("%d. %s", index.getAndIncrement(), entity.getName());
                });
    }

    public static void propHistogramPrintEntityProps(Entity entity) {
        AtomicInteger index = new AtomicInteger(1);

        entity.getInitialProperties().getPropsMap().values()
                .stream()
                .sorted(Comparator.comparing(Property::getName))
                .forEach(property -> {
                    System.out.printf("%d. %s", index.getAndIncrement(), property.getName());
                });
    }

    public static void printPropHistogram(HistoryManager historyManager, String simulationUUID,
                                          String entityName, String propertyName) throws UUIDNotFoundException {
        Map<String, Long> entitiesCountForProp =
                historyManager.getEntitiesCountForProp(simulationUUID, entityName, propertyName);

        System.out.printf("Entities [%s] count for property [%s] values%n:", entityName, propertyName);

        entitiesCountForProp.forEach((propValue, amount) -> {
            System.out.printf("Value [%s]: %d entities%n", propValue, amount);
        });
    }
}
