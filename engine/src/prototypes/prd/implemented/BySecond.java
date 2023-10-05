package prototypes.prd.implemented;

import prototypes.prd.generated.PRDBySecond;

import java.io.Serializable;

public class BySecond implements Serializable {
    protected int count;

    public BySecond(PRDBySecond bySecond) {
        count = bySecond.getCount();
    }

    public BySecond(BySecond other) {
        count = other.getCount();
    }

    public int getCount() {
        return count;
    }
}
