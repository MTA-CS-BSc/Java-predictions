package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDMultiply;

import java.io.Serializable;
import java.util.Objects;

public class Multiply implements Serializable {
    protected String arg1;
    protected String arg2;

    public Multiply(PRDMultiply multiply) {
        if (Objects.isNull(multiply)) {
            arg1 = "";
            arg2 = "";
        }

        else {
            arg1 = multiply.getArg1();
            arg2 = multiply.getArg2();
        }
    }

    public String getArg1() { return arg1; }
    public String getArg2() { return arg2; }
}
