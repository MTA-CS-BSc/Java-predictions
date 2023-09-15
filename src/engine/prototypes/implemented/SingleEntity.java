package engine.prototypes.implemented;

import engine.modules.RandomGenerator;
import engine.modules.Utils;

import java.io.Serializable;
import java.util.Objects;

public class SingleEntity implements Serializable {
    protected Properties properties;
    protected Coordinate coordinate;
    protected String entityName;
    public SingleEntity(SingleEntity other) {
        properties = new Properties(other.getProperties());
        coordinate = new Coordinate(other.coordinate);
        entityName = other.entityName;
    }
    public SingleEntity(String entityName, Properties fromProperties, WorldGrid worldGrid) {
        this.entityName = entityName;
        coordinate = RandomGenerator.randomizeRandomCoordinate(worldGrid);
        properties = new Properties(fromProperties);
        initRandomVars();
    }
    public Properties getProperties() {
        return properties;
    }
    public Coordinate getCoordinate() { return coordinate; }
    public void setCoordinate(Coordinate other) {
        coordinate.setX(other.getX());
        coordinate.setY(other.getY());
    }
    public void initRandomVars() {
        getProperties().getPropsMap().values().forEach(property -> {
            if (!property.getValue().isRandomInitialize()
                    && Objects.isNull(property.getValue().getInit()))
                property.getValue().setRandomInitialize(true);

            if (property.getValue().isRandomInitialize())
                Utils.setPropRandomInit(property, property.getRange());

            property.getValue().setCurrentValue(property.getValue().getInit());
        });
    }
    public String getEntityName() { return entityName; }
}
