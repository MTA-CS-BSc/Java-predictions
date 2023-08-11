package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDDivide;

import java.io.Serializable;
import java.util.Objects;

public class Divide implements Serializable {
    protected String arg1;
    protected String arg2;

    public Divide(PRDDivide divide) {
        if (Objects.isNull(divide)) {
            arg1 = "";
            arg2 = "";
        }

        else {
            arg1 = divide.getArg1();
            arg2 = divide.getArg2();
        }
    }

    public String getArg1() { return arg1; }
    public String getArg2() { return arg2; }

}
