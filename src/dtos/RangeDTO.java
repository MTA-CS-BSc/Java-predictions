package dtos;

public class RangeDTO {
    protected double to;

    protected double from;

    public RangeDTO(double from, double to) {
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
        return "Range: [" + getFrom() + ", " + getTo() + "]\n";
    }
}
