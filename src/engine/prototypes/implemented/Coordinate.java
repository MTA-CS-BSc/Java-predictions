package engine.prototypes.implemented;

import java.util.Objects;

public class Coordinate {
    protected int x;
    protected int y;
    public Coordinate(int _x, int _y) {
        x = _x;
        y = _y;
    }
    public Coordinate(Coordinate other) {
        x = other.x;
        y = other.y;
    }
    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int value) { x = value; }
    public void setY(int value) { y = value; }

    @Override
    public boolean equals(Object other) {
        if (Objects.isNull(other) || other.getClass() != Coordinate.class)
            return false;

        return x == ((Coordinate)other).getX() && y == ((Coordinate)other).getY();
    }
}
