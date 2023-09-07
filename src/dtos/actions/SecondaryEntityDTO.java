package dtos.actions;

public class SecondaryEntityDTO {
    protected String entityName;
    public SecondaryEntityDTO(String entityName) {
        this.entityName = entityName;
    }
    public String getEntityName() { return entityName; }
}
