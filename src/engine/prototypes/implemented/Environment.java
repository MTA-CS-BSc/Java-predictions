package engine.prototypes.implemented;

import engine.modules.Utils;
import engine.prototypes.jaxb.PRDEnvProperty;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Environment implements Serializable {
    protected Map<String, Property> envMap;
    public Environment(List<PRDEnvProperty> list) {
        envMap = new HashMap<>();

        for (PRDEnvProperty property : list)
            envMap.put(property.getPRDName(), new Property(property));
    }
    public Map<String, Property> getEnvVars() { return envMap; }
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
