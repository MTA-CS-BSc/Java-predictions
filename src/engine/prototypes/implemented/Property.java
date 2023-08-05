package engine.prototypes.implemented;

import engine.consts.Restrictions;
import engine.consts.PropTypes;
import engine.prototypes.jaxb.PRDEnvProperty;
import engine.prototypes.jaxb.PRDProperty;
import engine.prototypes.jaxb.PRDRange;
import engine.prototypes.jaxb.PRDValue;

import java.util.Objects;

public class Property {
    protected String name;
    protected PRDRange range;
    protected PRDValue value;
    protected String type;
    protected int stableTime;

    public Property(Object property) {
        if (property.getClass() == PRDProperty.class) {
            name = ((PRDProperty) property).getPRDName();
            type = ((PRDProperty) property).getType();
            value = ((PRDProperty) property).getPRDValue();
            range = ((PRDProperty) property).getPRDRange();

            value.setCurrentValue(value.getInit());
        }

        else if (property.getClass() == PRDEnvProperty.class) {
            name = ((PRDEnvProperty) property).getPRDName();
            type = ((PRDEnvProperty) property).getType();
            range = ((PRDEnvProperty) property).getPRDRange();
            value = new PRDValue();
            value.setRandomInitialize(true);
        }

        if (Objects.isNull(range) && PropTypes.NUMERIC_PROPS.contains(type))
            range = new PRDRange(Restrictions.MIN_RANGE, Restrictions.MAX_RANGE);

        stableTime = 0;
    }
    public String getName() { return name; }
    public String getType() { return type; }
    public PRDRange getRange() { return range; }
    public PRDValue getValue() { return value; }
    public int getStableTime() { return stableTime; }
    public void setStableTime(int value) { stableTime = value; }
}
