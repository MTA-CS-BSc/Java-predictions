package ui.printers;

import engine.consts.Restrictions;
import engine.prototypes.implemented.*;

import java.util.Objects;

public abstract class WorldDetailsPrinter {
    public static void print(World world) {
        System.out.println("--------------------------------------");
        System.out.println("----------Simulation details-----------");
        System.out.println("--------------------------------------");

        printEntities(world.getEntities());
        printRules(world.getRules());
        printTermination(world.getTermination());
    }
    private static void printEntities(Entities entities) {
        System.out.println("###########Entities details###########");

        entities.getEntitiesMap().values().forEach(entity -> {
            System.out.println("#####Entity######");
            System.out.println("Name: " + entity.getName());
            System.out.println("Population: " + entity.getPopulation());
            printEntityProps(entity);
        });
    }
    private static void printEntityProps(Entity entity) {
        System.out.println("###########Properties###########");

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
    private static void printTermination(Termination termination) {
        System.out.println("###########Termination###########");
        termination.getStopConditions().forEach(stopCondition -> {
           System.out.println("#####Stop Condition######");

           if (stopCondition.getClass() == ByTicks.class)
               System.out.printf("Stop after [%d] ticks%n", ((ByTicks)stopCondition).getCount());

           else if (stopCondition.getClass() == BySecond.class)
               System.out.printf("Stop after [%d] seconds%n", ((BySecond)stopCondition).getCount());
        });
    }
    private static void printRules(Rules rules) {
        System.out.println("###########Rules###########");

        rules.getRulesMap().values().forEach(rule -> {
            System.out.println("#####Rule######");

            System.out.println("Name: " + rule.getName());
            System.out.println("Activation ticks: " + rule.getActivation().getTicks());
            System.out.println("Activation probability: " + rule.getActivation().getProbability());
            System.out.println("Actions amount: " + rule.getActions().getActions().size());
        });
    }
}
