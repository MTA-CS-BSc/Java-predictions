package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDWorld;

public class WorldGrid {
    protected int rows;
    protected int columns;

    public WorldGrid(PRDWorld.PRDGrid grid) {
        rows = grid.getRows();
        columns = grid.getColumns();
    }
}
