package prototypes.prd.implemented;

import prototypes.prd.generated.PRDWorld;
import types.Coordinate;

public class WorldGrid {
    protected int rows;
    protected int columns;
    protected boolean[][] taken;

    public WorldGrid(PRDWorld.PRDGrid grid) {
        this(grid.getRows(), grid.getColumns());

    }

    public WorldGrid(WorldGrid other) {
        this.rows = other.getRows();
        this.columns = other.getColumns();

        taken = new boolean[rows][columns];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                taken[i][j] = other.getTaken()[i][j];
    }

    public WorldGrid(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        taken = new boolean[rows][columns];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                taken[i][j] = false;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean[][] getTaken() {
        return taken;
    }

    public boolean isTaken(Coordinate coordinate) {
        return taken[coordinate.getX()][coordinate.getY()];
    }

    public void changeCoordinateState(Coordinate coordinate) {
        taken[coordinate.getX()][coordinate.getY()] = !taken[coordinate.getX()][coordinate.getY()];
    }

    public void clear() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                taken[i][j] = false;
    }
}
