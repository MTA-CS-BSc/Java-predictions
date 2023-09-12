package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDRange;
import helpers.Constants;

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
            to = Constants.MAX_RANGE;
            from = Constants.MIN_RANGE;
        }
    }
    public Range (Range other) {
        if (!Objects.isNull(other)) {
            to = other.getTo();
            from = other.getFrom();
        }

        else {
            to = Constants.MAX_RANGE;
            from = Constants.MIN_RANGE;
        }
    }
    public double getTo() { return to; }
    public double getFrom() { return from; }
    @Override
    public String toString() {
        return "Range: [" + getFrom() + ", " + getTo() + "]\n";
    }
}
