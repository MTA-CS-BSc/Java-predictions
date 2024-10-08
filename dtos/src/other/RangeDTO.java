package other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RangeDTO {
    protected double to;

    protected double from;

    @JsonCreator
    public RangeDTO(@JsonProperty("from") double from,
                    @JsonProperty("to") double to) {
        this.to = to;
        this.from = from;
    }

    public double getTo() {
        return to;
    }

    public double getFrom() {
        return from;
    }

    @Override
    public String toString() {
        return String.format("[%.2f, %.2f]", from, to);
    }
}
