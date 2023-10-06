package types;

import modules.Restrictions;

public class Bounds {
    public int rowMDepth, rowPDepth, colMDepth, colPDepth;

    public Bounds() {
        rowMDepth = rowPDepth = colMDepth = colPDepth = Restrictions.NOT_SET;
    }

    public void setRowMDepth(int rowMDepth) {
        this.rowMDepth = rowMDepth;
    }

    public void setRowPDepth(int rowPDepth) {
        this.rowPDepth = rowPDepth;
    }

    public void setColMDepth(int colMDepth) {
        this.colMDepth = colMDepth;
    }

    public void setColPDepth(int colPDepth) {
        this.colPDepth = colPDepth;
    }
}
