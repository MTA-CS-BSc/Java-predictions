package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDEntity;

import java.util.HashMap;
import java.util.List;

public class Entities {
    protected HashMap<String, Entity> entitiesMap = new HashMap<>();

    public Entities(List<PRDEntity> list) {
        for (PRDEntity entity : list)
            entitiesMap.put(entity.getName(), new Entity(entity));
    }
    public HashMap<String, Entity> getEntitiesMap() { return entitiesMap; }
    public void setEntitiesMap(HashMap<String, Entity> value) { entitiesMap = value; }
}
