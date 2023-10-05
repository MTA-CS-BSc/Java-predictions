package prototypes.prd.implemented;

import modules.Constants;
import prototypes.prd.generated.PRDEnvProperty;
import prototypes.prd.generated.PRDProperty;
import prototypes.prd.generated.PRDValue;
import types.PropTypes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Property implements Serializable {
    protected int stableTime;
    protected String name;
    protected Range range;
    protected Value value;
    protected String type;
    protected List<Long> changesTicks;

    public Property(PRDProperty property) {
        changesTicks = new ArrayList<>();
        name = property.getPRDName();
        type = property.getType();
        value = new Value(property.getPRDValue());

        if (PropTypes.NUMERIC_PROPS.contains(type))
            range = new Range(property.getPRDRange());
    }

    public Property(PRDEnvProperty envProperty) {
        changesTicks = new ArrayList<>();
        name = envProperty.getPRDName();
        type = envProperty.getType();
        value = new Value(new PRDValue());
        value.setRandomInitialize(true);

        if (PropTypes.NUMERIC_PROPS.contains(envProperty.getType()))
            range = new Range(envProperty.getPRDRange());
    }

    public Property(Property other) {
        changesTicks = new ArrayList<>();
        name = other.getName();
        type = other.getType();
        stableTime = 0;
        value = new Value(other.getValue());

        if (PropTypes.NUMERIC_PROPS.contains(other.getType()))
            range = new Range(other.getRange());
    }

    public void addChangeTick(long tick) {
        changesTicks.add(tick);
    }

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

    public double getConsistency(long simulationTicks) {
        double res = 0.0;

        if (changesTicks.size() == 0)
            return simulationTicks;

        for (int i = 0; i < changesTicks.size() - 1; i++)
            res += changesTicks.get(i + 1) - changesTicks.get(i);

        res += simulationTicks - changesTicks.get(changesTicks.size() - 1);
        res += changesTicks.get(0);

        return res / changesTicks.size();
    }

    public boolean hasNoRange() {
        return Objects.isNull(getRange())
                || (getRange().getTo() == Constants.MAX_RANGE && getRange().getFrom() == Constants.MIN_RANGE);
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
