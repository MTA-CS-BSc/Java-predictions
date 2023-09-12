package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDBySecond;

import java.io.Serializable;

public class BySecond implements Serializable {
    protected int count;
    public BySecond(PRDBySecond bySecond) {
        count = bySecond.getCount();
    }

    public BySecond(BySecond other) {
        count = other.getCount();
    }

    public int getCount() { return count; }
}
