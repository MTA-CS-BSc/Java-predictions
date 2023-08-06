package engine.prototypes.implemented;

import engine.modules.Utils;
import engine.prototypes.jaxb.PRDEnvProperty;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDRange;
import engine.prototypes.jaxb.PRDValue;

public class Property {
    protected int stableTime;
    protected String name;
    protected PRDRange range;
    protected PRDValue value;
    protected String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PRDRange getRange() {
        return range;
    }

    public void setRange(PRDRange range) {
        this.range = range;
    }

    public PRDValue getValue() {
        return value;
    }

    public void setValue(PRDValue value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStableTime() {
        return stableTime;
    }

    public void setStableTime(int stableTime) {
        this.stableTime = stableTime;
    }

    public Property(PRDProperty property) {
        name = property.getPRDName();
        range = property.getPRDRange();
        type = property.getType();
        value = new PRDValue();
        value.setRandomInitialize(property.getPRDValue().isRandomInitialize());

        if (value.isRandomInitialize())
            Utils.setPropRandomInit(this, range);

        else
            value.setInit(property.getPRDValue().getInit());

        value.setCurrentValue(value.getInit());
    }

    public Property(PRDEnvProperty envProperty) {
        name = envProperty.getPRDName();
        range = envProperty.getPRDRange();
        type = envProperty.getType();
        value = new PRDValue();

        value.setRandomInitialize(true);
    }

}
