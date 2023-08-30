package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDAction;

import java.util.Objects;

public abstract class Action {
    protected String type;
    protected SecondaryEntity secondaryEntity;
    public Action(String type, PRDAction.PRDSecondaryEntity secondaryEntity) {
        this.type = type;

        if (!Objects.isNull(secondaryEntity))
            this.secondaryEntity = new SecondaryEntity(secondaryEntity);
    }
    public String getType() { return type; }
    public SecondaryEntity getSecondaryEntity() { return secondaryEntity; }
}