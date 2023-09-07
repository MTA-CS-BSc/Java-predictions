package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDByTicks;

import java.io.Serializable;
import java.util.Objects;

public class ByTicks implements Serializable {
    protected int count;
    public ByTicks(PRDByTicks byTicks) {
        if (!Objects.isNull(byTicks))
            count = byTicks.getCount();
    }
    public int getCount() { return count; }
}
