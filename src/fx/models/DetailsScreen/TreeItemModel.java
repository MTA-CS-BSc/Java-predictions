package fx.models.DetailsScreen;

public class TreeItemModel {
    private final String name;

    public TreeItemModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
