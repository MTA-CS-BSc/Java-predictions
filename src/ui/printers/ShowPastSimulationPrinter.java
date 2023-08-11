package ui.printers;

import com.sun.xml.internal.ws.util.StringUtils;
import engine.exceptions.UUIDNotFoundException;
import engine.history.HistoryManager;
import engine.modules.Utils;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.Property;
import engine.simulation.SingleSimulation;
import ui.enums.MainMenu;
import ui.enums.PastSimulationOutputOptions;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ShowPastSimulationPrinter {
    public static void printAvailableSimulations(HistoryManager historyManager) {
        AtomicInteger index = new AtomicInteger(1);

        historyManager.getPastSimulations().values()
                .stream()
                .sorted(Comparator.comparing(s -> s.getStartTimestamp()))
                .forEach(simulation -> {
                    System.out.printf("%d. UUID: [%s], timestamp: [%s]%n",
                            index.getAndIncrement(), simulation.getUUID(),
                            simulation.getStartTimestamp());
                });

        System.out.println("Please select your choice:");
    }

    public static void printOutputOptions() {
        System.out.println("Please select an output option: ");

        Arrays.stream(PastSimulationOutputOptions.values())
                .forEach(element -> {
                    System.out.printf("%d -> %s%n", element.ordinal() + 1,
                            StringUtils.capitalize(element.name().replace("_", " ").toLowerCase()));
                });
    }

    public static void printEntitiesBeforeAfter(HistoryManager historyManager, String simulationUUID) throws UUIDNotFoundException {
        Map<String, Integer[]> entitiesBeforeAndAfter = historyManager.getEntitiesBeforeAndAfter(simulationUUID);

        entitiesBeforeAndAfter.forEach((entityName, amount) -> {
            System.out.printf("Entity [%s]: %d->%d%n", entityName, amount[0], amount[1]);
        });
    }

    public static void propHistogramPrintEntities(SingleSimulation simulation) {
        System.out.println("Available entities: ");
        AtomicInteger index = new AtomicInteger(1);

        simulation.getStartWorldState().getEntitiesMap()
                .values()
                .stream()
                .sorted(Comparator.comparing(Entity::getName))
                .forEach(entity -> {
                    System.out.printf("%d. %s%n", index.getAndIncrement(), entity.getName());
                });

        System.out.println("Please select your choice:");
    }

    public static void propHistogramPrintEntityProps(Entity entity) {
        System.out.printf("Available properties for entity [%s]:\n ", entity.getName());
        AtomicInteger index = new AtomicInteger(1);

        entity.getInitialProperties().getPropsMap().values()
                .stream()
                .sorted(Comparator.comparing(Property::getName))
                .forEach(property -> {
                    System.out.printf("%d. %s%n", index.getAndIncrement(), property.getName());
                });
    }

    public static void printPropHistogram(HistoryManager historyManager, String simulationUUID,
                                          String entityName, String propertyName) throws UUIDNotFoundException {
        Map<String, Long> entitiesCountForProp =
                historyManager.getEntitiesCountForProp(simulationUUID, entityName, propertyName);

        System.out.printf("Entities [%s] count for property [%s] values:%n", entityName, propertyName);

        entitiesCountForProp.forEach((propValue, amount) -> {
            System.out.printf("Value [%s]: %d entities%n", propValue, amount);
        });

        System.out.println("--------------------------");
    }
}
