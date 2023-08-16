package dtos;

public class PropertyDTO {
    protected String name;
    protected String type;

    protected RangeDTO range;

    public PropertyDTO(String _name, String _type, RangeDTO _range) {
        name = _name;
        type = _type;
        range = _range;
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
}
