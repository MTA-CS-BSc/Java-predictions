package ui.printers;

import engine.EngineAPI;
import engine.prototypes.PropertyDTO;
import engine.prototypes.implemented.Environment;
import engine.prototypes.implemented.Property;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class EnvPropsInitializerPrinter {
    public static int printEnvironmentProps(EngineAPI api, String uuid) {
        AtomicInteger index = new AtomicInteger(1);
        List<PropertyDTO> props = api.getEnvironmentProperties(uuid);

        props.forEach(property -> {
            System.out.printf("%d. %s %s%n", index.getAndIncrement(),
                    property.getType(), property.getName());
        });

        System.out.println(index.get() + ". I'm done!");
        return index.get() - 1;
    }
}
