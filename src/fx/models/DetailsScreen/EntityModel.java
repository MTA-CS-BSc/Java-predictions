package fx.models.DetailsScreen;

import java.util.List;

public class EntityModel extends TreeItemModel {
    private final List<EntityPropertyModel> properties;
    private int population;

    public EntityModel(String name, List<EntityPropertyModel> properties) {
        super(name);
        this.properties = properties;
        this.population = 0;
    }

    public List<EntityPropertyModel> getProperties() {
        return properties;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int value) {
        population = value;
    }
}
