package engine.prototypes.implemented;

import engine.consts.Restrictions;
import engine.prototypes.jaxb.PRDRange;

import java.io.Serializable;
import java.util.Objects;

public class Range implements Serializable {
    protected double to;
    protected double from;

    public Range(PRDRange range) {
        if (!Objects.isNull(range)) {
            to = range.getTo();
            from = range.getFrom();
        }

        else {
            to = Restrictions.MAX_RANGE;
            from = Restrictions.MIN_RANGE;
        }
    }

    public double getTo() { return to; }
    public double getFrom() { return from; }
    @Override
    public String toString() {
        return "Range: [" + getFrom() + ", " + getTo() + "]\n";
    }
}
