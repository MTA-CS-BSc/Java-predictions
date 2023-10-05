package simulation;

import prototypes.prd.implemented.Entity;
import prototypes.prd.implemented.Property;
import prototypes.prd.implemented.World;
import prototypes.prd.implemented.WorldGrid;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WorldState implements Serializable {
    protected Map<String, Entity> entitiesMap;
    protected Map<String, Property> environmentMap;
    protected WorldGrid grid;

    public WorldState(World world) {
        entitiesMap = new HashMap<>();
        environmentMap = new HashMap<>();
        grid = new WorldGrid(world.getGrid());
        world.getEntities().getEntitiesMap().forEach((key, entity) -> entitiesMap.put(key, new Entity(entity)));
        world.getEnvironment().getEnvVars().forEach((key, envVar) -> environmentMap.put(key, new Property(envVar)));
    }

    public Map<String, Entity> getEntitiesMap() {
        return entitiesMap;
    }

    public Map<String, Property> getEnvironmentMap() {
        return environmentMap;
    }

    public WorldGrid getGrid() {
        return grid;
    }
}
