package prototypes.prd.implemented;

import prototypes.prd.generated.PRDByTicks;

import java.io.Serializable;

public class ByTicks implements Serializable {
    protected int count;

    public ByTicks(PRDByTicks byTicks) {
        count = byTicks.getCount();
    }

    public ByTicks(int count) {
        this.count = count;
    }

    public ByTicks(ByTicks other) {
        count = other.getCount();
    }

    public int getCount() {
        return count;
    }
}
