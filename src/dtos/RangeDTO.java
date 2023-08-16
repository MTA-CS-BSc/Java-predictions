package dtos;

public class RangeDTO {
    protected double to;

    protected double from;

    public RangeDTO(double _from, double _to) {
        to = _to;
        from = _from;
    }

    public double getTo() {
        return to;
    }

    public double getFrom() {
        return from;
    }
}
