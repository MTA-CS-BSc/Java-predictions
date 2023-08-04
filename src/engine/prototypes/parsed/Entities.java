package engine.prototypes.parsed;

import engine.prototypes.jaxb.PRDEntity;

import java.util.HashMap;
import java.util.List;

public class Entities {
    protected HashMap<String, Entity> entities = new HashMap<>();

    public Entities(List<PRDEntity> list) {
        for (PRDEntity entity : list)
            entities.put(entity.getName(), new Entity(entity));
    }

    public HashMap<String, Entity> getEntities() { return entities; }

    public void setEntities(HashMap<String, Entity> value) { entities = value; }
}
