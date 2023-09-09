package dtos.actions;

public class SecondaryEntityDTO {
    protected String entityName;
    public SecondaryEntityDTO(String entityName) {
        this.entityName = entityName;
    }
    public SecondaryEntityDTO(SecondaryEntityDTO other) {
        this.entityName = other.getEntityName();
    }
    public String getEntityName() { return entityName; }
}
