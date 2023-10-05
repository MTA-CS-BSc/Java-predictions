package prototypes.prd.implemented;

import modules.Utils;
import prototypes.prd.generated.PRDEnvProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Environment implements Serializable {
    protected Map<String, Property> envMap;

    public Environment() {
        envMap = new HashMap<>();
    }

    public Environment(List<PRDEnvProperty> list) {
        this();

        for (PRDEnvProperty property : list)
            envMap.put(property.getPRDName(), new Property(property));
    }

    public Map<String, Property> getEnvVars() {
        return envMap;
    }

    public void initRandomVars() {
        envMap.values().forEach(property -> {
            if (!property.getValue().isRandomInitialize()
                    && Objects.isNull(property.getValue().getInit()))
                property.getValue().setRandomInitialize(true);

            if (property.getValue().isRandomInitialize())
                Utils.setPropRandomInit(property, property.getRange());

            property.getValue().setCurrentValue(property.getValue().getInit());
        });
    }
}
