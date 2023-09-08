package dtos.actions;

import java.util.Objects;

public class ActionDTO {
    protected String type;
    protected String entityName;
    protected SecondaryEntityDTO secondaryEntity;
    protected boolean secondaryEntityExists;
    public ActionDTO(String type, String entityName, SecondaryEntityDTO secondaryEntity) {
        this.type = type;
        this.entityName = entityName;
        this.secondaryEntity = secondaryEntity;
        this.secondaryEntityExists = !Objects.isNull(secondaryEntity);
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
    public boolean isSecondaryEntityExists() {
        return secondaryEntityExists;
    }
}
