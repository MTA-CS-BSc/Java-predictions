package engine.prototypes.implemented;

import java.util.HashMap;

public class WorldState {
    protected HashMap<String, Entity> entitiesMap;

    public WorldState(World world) {
        entitiesMap = new HashMap<>();
        world.getEntities().entitiesMap.forEach((key, entity) -> entitiesMap.put(key, new Entity(entity)));
    }

    public HashMap<String, Entity> getEntitiesMap() {
        return entitiesMap;
    }
}
