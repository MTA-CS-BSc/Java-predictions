package prototypes.prd.implemented;

import prototypes.prd.generated.PRDEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Entities implements Serializable {
    protected Map<String, Entity> entitiesMap;

    public Entities() {
        entitiesMap = new HashMap<>();
    }

    public Entities(List<PRDEntity> list) {
        this();

        for (PRDEntity entity : list)
            entitiesMap.put(entity.getName(), new Entity(entity));
    }

    public Map<String, Entity> getEntitiesMap() {
        return entitiesMap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("###########Entities###########\n");

        entitiesMap.values().forEach(entity -> sb.append(entity.toString()).append("\n"));

        return sb.toString();
    }
}
