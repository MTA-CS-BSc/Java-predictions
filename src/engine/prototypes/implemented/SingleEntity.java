package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDProperty;
import helpers.Constants;

import java.io.Serializable;
import java.util.List;

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
        //TODO: Add randomized coordinates
        coordinate.setX(x);
        coordinate.setY(y);
    }
}
