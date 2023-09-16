package fx.models.DetailsScreen;

public class RangeModel extends TreeItemModel {
    private final double from;
    private final double to;

    public RangeModel(double from, double to) {
        super("Range");
        this.from = from;
        this.to = to;
    }

    public double getFrom() {
        return from;
    }

    public double getTo() {
        return to;
    }

    @Override
    public String toString() {
        return String.format("[%.2f, %.2f]", from, to);
    }
}
