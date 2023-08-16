package dtos;

public class PropertyDTO {
    protected String name;
    protected String type;

    public PropertyDTO(String _name, String _type) {
        name = _name;
        type = _type;
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
}
