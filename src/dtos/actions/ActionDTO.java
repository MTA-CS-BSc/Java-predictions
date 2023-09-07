package dtos.actions;

public abstract class ActionDTO {
    protected String type;
    protected String entityName;
    protected SecondaryEntityDTO secondaryEntity;
    public ActionDTO(String type, String entityName, SecondaryEntityDTO secondaryEntity) {
        this.type = type;
        this.entityName = entityName;
        this.secondaryEntity = secondaryEntity;
    }
    public String getType() {
        return type;
    }
    public String getEntityName() {
        return entityName;
    }
    public SecondaryEntityDTO getSecondaryEntity() {
        return secondaryEntity;
    }
}
