package prototypes.prd.implemented;

import prototypes.prd.generated.PRDValue;

import java.io.Serializable;

public class Value implements Serializable {
    protected boolean isRandomInitialize;
    protected String init;
    protected String currentValue;

    public Value(PRDValue value) {
        isRandomInitialize = value.isRandomInitialize();
        init = value.getInit();
        currentValue = value.getInit();
    }

    public Value(Value other) {
        isRandomInitialize = other.isRandomInitialize();
        init = other.getInit();
        currentValue = other.getCurrentValue();
    }

    public boolean isRandomInitialize() {
        return isRandomInitialize;
    }

    public void setRandomInitialize(boolean value) {
        isRandomInitialize = value;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String value) {
        init = value;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String value) {
        currentValue = value;
    }
}
