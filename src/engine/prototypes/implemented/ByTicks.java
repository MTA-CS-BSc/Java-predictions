package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDByTicks;

import java.io.Serializable;

public class ByTicks implements Serializable {
    protected int count;

    public ByTicks(PRDByTicks byTicks) {
        count = byTicks.getCount();
    }

    public ByTicks(ByTicks other) {
        count = other.getCount();
    }

    public int getCount() {
        return count;
    }
}
