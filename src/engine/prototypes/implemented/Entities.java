package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEntity;

import java.util.HashMap;
import java.util.List;

public class Entities {
    protected HashMap<String, Entity> entitiesMap;
    public Entities(List<PRDEntity> list) {
        entitiesMap = new HashMap<>();

        for (PRDEntity entity : list)
            entitiesMap.put(entity.getName(), new Entity(entity));
    }
    public HashMap<String, Entity> getEntitiesMap() { return entitiesMap; }
}
