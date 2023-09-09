package engine.prototypes.implemented;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WorldState implements Serializable {
    protected Map<String, Entity> entitiesMap;
    protected Map<String, Property> environmentMap;

    public WorldState(World world) {
        entitiesMap = new HashMap<>();
        environmentMap = new HashMap<>();
        world.getEntities().getEntitiesMap().forEach((key, entity) -> entitiesMap.put(key, new Entity(entity)));
        world.getEnvironment().getEnvVars().forEach((key, envVar) -> environmentMap.put(key, new Property(envVar)));
    }

    public Map<String, Entity> getEntitiesMap() {
        return entitiesMap;
    }
    public Map<String, Property> getEnvironmentMap() { return environmentMap; }
}
