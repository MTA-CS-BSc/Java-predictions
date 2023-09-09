package dtos.actions;

public class IncreaseDecreaseDTO extends ActionDTO {
    protected String propertyName;
    protected String by;
    public IncreaseDecreaseDTO(String type, String entityName, SecondaryEntityDTO secondaryEntity,
                               String propertyName, String by) {
        super(type, entityName, secondaryEntity);
        this.propertyName = propertyName;
        this.by = by;
    }
    public String getPropertyName() {
        return propertyName;
    }
    public String getBy() {
        return by;
    }
}
