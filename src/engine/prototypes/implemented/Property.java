package engine.prototypes.implemented;

import helpers.PropTypes;
import engine.modules.Utils;
import engine.prototypes.jaxb.PRDEnvProperty;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDValue;
import helpers.Constants;

import java.io.Serializable;
import java.util.Objects;

public class Property implements Serializable {
    protected int stableTime;
    protected String name;
    protected Range range;
    protected Value value;
    protected String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
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

    public boolean hasNoRange() {
        return Objects.isNull(getRange())
                || (getRange().getTo() == Constants.MAX_RANGE && getRange().getFrom() == Constants.MIN_RANGE);
    }

    public Property(PRDProperty property) {
        name = property.getPRDName();
        type = property.getType();
        value = new Value(property.getPRDValue());

        if (PropTypes.NUMERIC_PROPS.contains(type))
            range = new Range(property.getPRDRange());

        value.setRandomInitialize(property.getPRDValue().isRandomInitialize());

        if (value.isRandomInitialize())
            Utils.setPropRandomInit(this, range);

        else {
            if (PropTypes.NUMERIC_PROPS.contains(type))
                property.getPRDValue().setInit(Utils.removeExtraZeroes(property.getPRDValue().getInit()));

            value.setInit(property.getPRDValue().getInit());
        }

        value.setCurrentValue(value.getInit());
    }

    public Property(PRDEnvProperty envProperty) {
        name = envProperty.getPRDName();

        if (PropTypes.NUMERIC_PROPS.contains(envProperty.getType()))
            range = new Range(envProperty.getPRDRange());

        type = envProperty.getType();
        value = new Value(new PRDValue());
        value.setRandomInitialize(true);
    }

    public Property(Property other) {
        name = other.getName();
        type = other.getType();
        value = new Value(other.getValue());

        value.setRandomInitialize(true);

        if (!Objects.isNull(other.getValue())) {
            value.setRandomInitialize(other.getValue().isRandomInitialize());
            value.setInit(other.getValue().getInit());
            value.setCurrentValue(other.getValue().getCurrentValue());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#####Property#####\n").append("Name: ").append(getName()).append("\n").append("Type: ")
                .append(getType()).append("\n").append("Is Random initialize: ").append(getValue().isRandomInitialize()).append("\n");


        if (!Objects.isNull(getRange()))
            sb.append(getRange().toString());

        return sb.toString();
    }
}
