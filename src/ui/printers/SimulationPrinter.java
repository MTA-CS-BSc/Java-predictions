package ui.printers;

import engine.consts.Restrictions;
import engine.prototypes.implemented.Entities;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.World;

import java.util.Objects;

public abstract class SimulationPrinter {
    public static void print(World world) {
        System.out.println("--------------------------------------");
        System.out.println("----------Simulation details-----------");
        System.out.println("--------------------------------------");

        printEntities(world.getEntities());
       // printRules(world.getRules());
        //printTermination(world.getTermination());
    }

    private static void printEntities(Entities entities) {
        System.out.println("***Entities details***");

        entities.getEntitiesMap().values().forEach(entity -> {
            System.out.println("++++++++Entity++++++++");
            System.out.println("Name: " + entity.getName());
            System.out.println("Population: " + entity.getPopulation());
            printEntityProps(entity);
        });
    }
    private static void printEntityProps(Entity entity) {
        System.out.println("Properties: ");

        entity.getInitialProperties().getPropsMap().values().forEach(property -> {
            System.out.println("#####Property#####");
            System.out.println("Name: " + property.getName());
            System.out.println("Type: " + property.getType());
            System.out.println("Is random initialize: " + property.getValue().isRandomInitialize());

            if (Objects.isNull(property.getRange())) {
                if (property.getRange().getFrom() != (double)Restrictions.MIN_RANGE
                    || property.getRange().getTo() != (double)Restrictions.MAX_RANGE)
                    System.out.printf("Range: [%s, %s]%n", property.getRange().getFrom(),
                            property.getRange().getTo());
            }
        });
    }
}
