package engine.prototypes.implemented;

import engine.modules.Utils;
import engine.prototypes.jaxb.PRDValue;

import java.io.Serializable;
import java.util.Objects;

public class Value implements Serializable {
    protected boolean isRandomInitialize;
    protected String init;
    protected String currentValue;

    public Value(PRDValue _value) {
        if (!Objects.isNull(_value)) {
            isRandomInitialize = _value.isRandomInitialize();
            init = _value.getInit();
            currentValue = _value.getInit();
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
    public void setInit(String value) {
        if (!value.isEmpty() && value.matches(Utils.REGEX_ONLY_ZEROES_AFTER_DOT))
            value = value.split("\\.")[0];

        init = value;
    }

    public void setCurrentValue(String value) { currentValue = value; }
}
