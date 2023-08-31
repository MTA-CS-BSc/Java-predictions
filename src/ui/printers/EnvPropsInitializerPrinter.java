package ui.printers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dtos.PropertyDTO;
import engine.EngineAPI;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class EnvPropsInitializerPrinter {
    public static int printEnvironmentProps(EngineAPI api, String uuid) {
        AtomicInteger index = new AtomicInteger(1);
        List<PropertyDTO> props = new Gson().fromJson(api.getEnvironmentProperties(uuid).getData(), new TypeToken<List<PropertyDTO>>(){}.getType());

        props.forEach(property -> {
            if (!Objects.isNull(property.getRange()) && !property.hasNoRange())
                System.out.printf("%d. %s %s, range: [%.2f, %.2f]%n", index.getAndIncrement(),
                        property.getType(), property.getName(), property.getRange().getFrom(), property.getRange().getTo());

            else
                System.out.printf("%d. %s %s%n", index.getAndIncrement(), property.getType(), property.getName());
        });

        System.out.println(index.get() + ". I'm done!");
        return index.get() - 1;
    }
}
