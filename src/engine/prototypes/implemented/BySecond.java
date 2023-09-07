package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDBySecond;

import java.io.Serializable;
import java.util.Objects;

public class BySecond implements Serializable {
    protected int count;
    public BySecond(PRDBySecond bySecond) {
        if (!Objects.isNull(bySecond))
            count = bySecond.getCount();
    }

    public int getCount() { return count; }
}
