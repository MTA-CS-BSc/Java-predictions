package engine.prototypes.implemented;

import engine.modules.Utils;
import engine.prototypes.jaxb.PRDProperty;
import helpers.Constants;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SingleEntity implements Serializable {
    protected Properties properties;
    protected Coordinate coordinate;
    public SingleEntity(List<PRDProperty> props) {
        properties = new Properties(props);
        coordinate = new Coordinate(Constants.NOT_SET, Constants.NOT_SET);
    }
    public SingleEntity(SingleEntity other) {
        properties = new Properties(other.getProperties());
        coordinate = new Coordinate(other.coordinate);
    }
    public SingleEntity(Properties fromProperties) {
        properties = new Properties(fromProperties);
        coordinate = new Coordinate(Constants.NOT_SET, Constants.NOT_SET);
    }
    public Properties getProperties() {
        return properties;
    }
    public Coordinate getCoordinate() { return coordinate; }
    public void setCoordinate(int x, int y) {
        //TODO: Add randomized coordinates + check for not same coordinate
        coordinate.setX(x);
        coordinate.setY(y);
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
}
