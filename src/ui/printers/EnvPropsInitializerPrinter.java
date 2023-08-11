package ui.printers;

import engine.prototypes.implemented.Environment;
import engine.prototypes.implemented.Property;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class EnvPropsInitializerPrinter {
    public static int printEnvs(Environment env) {
        AtomicInteger index = new AtomicInteger(1);

        env.getEnvVars().values()
                .stream()
                .sorted(Comparator.comparing(Property::getName))
                .forEach(property -> {
                   System.out.printf("%d. %s %s%n", index.getAndIncrement(),
                           property.getType(), property.getName());
                });

        System.out.println(index.get() + ". I'm done!");
        return index.get() - 1;
    }
}
