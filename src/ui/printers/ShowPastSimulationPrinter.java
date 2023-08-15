package ui.printers;

import com.sun.xml.internal.ws.util.StringUtils;
import engine.EngineAPI;
import engine.exceptions.UUIDNotFoundException;
import engine.prototypes.EntityDTO;
import engine.simulation.SingleSimulationDTO;
import ui.enums.PastSimulationOutputOptions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ShowPastSimulationPrinter {
    public static void printAvailableSimulations(EngineAPI api) {
        List<SingleSimulationDTO> simulations = api.getPastSimulations();
        AtomicInteger index = new AtomicInteger(1);

        simulations.forEach(simulation -> {
            System.out.printf("%d. UUID: %s, Timestamp: %s", index.getAndIncrement(),
                    simulation.getUuid(), simulation.getStartTimestamp());
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
    public static void printEntitiesBeforeAndAfterSimulation(EngineAPI api, String uuid) throws UUIDNotFoundException {
        Map<String, Integer[]> entitiesBeforeAndAfter = api.getEntitiesBeforeAndAfterSimulation(uuid);

        entitiesBeforeAndAfter.forEach((entityName, amount) -> {
            System.out.printf("Entity [%s]: %d->%d%n", entityName, amount[0], amount[1]);
        });
    }
    public static void propHistogramPrintEntities(EngineAPI api, String uuid) {
        System.out.println("Available entities: ");
        AtomicInteger index = new AtomicInteger(1);

        api.getEntities(uuid).forEach(entity -> System.out.printf("%d. %s%n", index.getAndIncrement(), entity.getName()));
        System.out.println("Please select your choice:");
    }
    public static void propHistogramPrintEntityProps(EntityDTO entity) {
        System.out.printf("Available properties for entity [%s]:\n ", entity.getName());
        AtomicInteger index = new AtomicInteger(1);

        entity.getProperties().forEach(property -> System.out.printf("%d. %s%n", index.getAndIncrement(), property.getName()));
        System.out.println("Please select your choice: ");
    }
    public static void printPropHistogram(EngineAPI api, String simulationUUID,
                                          String entityName, String propertyName) throws UUIDNotFoundException {
        Map<String, Long> entitiesCountForProp =
                api.getEntitiesCountForProp(simulationUUID, entityName, propertyName);

        System.out.printf("Entities [%s] count for property [%s] values:%n", entityName, propertyName);

        entitiesCountForProp.forEach((propValue, amount) -> {
            System.out.printf("Value [%s]: %d entities%n", propValue, amount);
        });

        System.out.println("--------------------------");
    }
}
