package dtos;

import helpers.Constants;

import java.util.Objects;

public class PropertyDTO {
    protected String name;
    protected String type;
    protected String value;
    protected RangeDTO range;
    protected boolean isRandom;

    public PropertyDTO(String name, String type, RangeDTO range, String value, boolean isRandom) {
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#####Property#####\n").append("Name: ").append(getName()).append("\n").append("Type: ")
                .append(getType()).append("\n").append("Is Random initialize: ").append(getIsRandom()).append("\n");

        if (!Objects.isNull(getRange()))
            sb.append(getRange().toString());

        return sb.toString();
    }
}
