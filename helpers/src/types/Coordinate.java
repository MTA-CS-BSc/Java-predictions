package types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Coordinate {
    protected int x;
    protected int y;

    @JsonCreator
    public Coordinate(@JsonProperty("x") int x,
                      @JsonProperty("y") int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate other) {
        x = other.x;
        y = other.y;
    }

    public int getX() {
        return x;
    }

    public void setX(int value) {
        x = value;
    }

    public int getY() {
        return y;
    }

    public void setY(int value) {
        y = value;
    }

    @Override
    public boolean equals(Object other) {
        if (Objects.isNull(other) || other.getClass() != Coordinate.class)
            return false;

        return x == ((Coordinate) other).getX() && y == ((Coordinate) other).getY();
    }
}
