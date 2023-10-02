package prototypes.implemented;

import prototypes.jaxb.PRDMultiply;

import java.io.Serializable;

public class Multiply implements Serializable {
    protected String arg1;
    protected String arg2;

    public Multiply(PRDMultiply multiply) {
        arg1 = multiply.getArg1();
        arg2 = multiply.getArg2();
    }

    public Multiply(Multiply other) {
        arg1 = other.getArg1();
        arg2 = other.getArg2();
    }

    public String getArg1() {
        return arg1;
    }

    public String getArg2() {
        return arg2;
    }
}
