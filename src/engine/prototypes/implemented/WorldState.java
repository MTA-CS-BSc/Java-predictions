package engine.prototypes.implemented;

import java.util.HashMap;
import java.util.Map;

public class WorldState {
    protected Map<String, Entity> entitiesMap;

    public WorldState(World world) {
        entitiesMap = new HashMap<>();
        world.getEntities().entitiesMap.forEach((key, entity) -> entitiesMap.put(key, new Entity(entity)));
    }

    public Map<String, Entity> getEntitiesMap() {
        return entitiesMap;
    }
}
