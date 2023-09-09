package dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import helpers.Constants;

import java.util.Objects;

public class PropertyDTO {
    protected String name;
    protected String type;
    protected String value;
    protected RangeDTO range;
    protected boolean isRandom;

    @JsonCreator
    public PropertyDTO(@JsonProperty("name") String name,
                       @JsonProperty("type") String type,
                       @JsonProperty("range") RangeDTO range,
                       @JsonProperty("value") String value,
                       @JsonProperty("isRandom") boolean isRandom) {
        this.name = name;
        this.type = type;
        this.range = range;
        this.value = value;
        this.isRandom = isRandom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RangeDTO getRange() {
        return range;
    }

    public String getValue() { return value; }

    public void setValue(String value) { this.value = value; }

    public boolean getIsRandom() { return isRandom; }

    public void setIsRandom(boolean value) { this.isRandom = value; }

    public boolean hasNoRange() {
        return Objects.isNull(getRange())
                || (getRange().getTo() == Constants.MAX_RANGE && getRange().getFrom() == Constants.MIN_RANGE);
    }
}
