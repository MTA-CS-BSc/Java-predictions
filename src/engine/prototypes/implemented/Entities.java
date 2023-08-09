package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entities {
    protected Map<String, Entity> entitiesMap;
    public Entities(List<PRDEntity> list) {
        entitiesMap = new HashMap<>();

        for (PRDEntity entity : list)
            entitiesMap.put(entity.getName(), new Entity(entity));
    }
    public Map<String, Entity> getEntitiesMap() { return entitiesMap; }
}
