package prototypes.implemented;

import prototypes.jaxb.PRDDivide;

import java.io.Serializable;

public class Divide implements Serializable {
    protected String arg1;
    protected String arg2;

    public Divide(PRDDivide divide) {
        arg1 = divide.getArg1();
        arg2 = divide.getArg2();
    }

    public Divide(Divide other) {
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
