package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDValue;

import java.util.Objects;

public class Value {
    protected boolean isRandomInitialize;
    protected String init;

    protected String currentValue;

    public Value(PRDValue _value) {
        if (!Objects.isNull(_value)) {
            isRandomInitialize = _value.isRandomInitialize();
            init = _value.getInit();
            currentValue = _value.getCurrentValue();
        }
    }

    public Value(Value other) {
        if (!Objects.isNull(other)) {
            isRandomInitialize = other.isRandomInitialize();
            init = other.getInit();
            currentValue = other.getCurrentValue();
        }
    }

    public boolean isRandomInitialize() {
        return isRandomInitialize;
    }

    public String getInit() {
        return init;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setRandomInitialize(boolean value) { isRandomInitialize = value; }

    public void setInit(String value) { init = value; }

    public void setCurrentValue(String value) { currentValue = value; }
}
