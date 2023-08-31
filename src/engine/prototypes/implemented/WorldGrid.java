package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDWorld;

public class WorldGrid {
    protected int rows;
    protected int columns;
    protected boolean[][] taken;

    public WorldGrid(PRDWorld.PRDGrid grid) {
        rows = grid.getRows();
        columns = grid.getColumns();
        taken = new boolean[rows][columns];

        for (int i = 0 ; i < rows; i++)
            for (int j = 0; j < columns; j++)
                taken[i][j] = false;
    }
    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
    public boolean isTaken(Coordinate coordinate) {
        return taken[coordinate.getX()][coordinate.getY()];
    }
    public void changeCoordinateState(Coordinate coordinate) {
        taken[coordinate.getX()][coordinate.getY()] = !taken[coordinate.getX()][coordinate.getY()];
    }
}
