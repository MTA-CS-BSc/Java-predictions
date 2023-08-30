package engine.prototypes.implemented.actions;

import engine.prototypes.implemented.SecondaryEntity;
import engine.prototypes.jaxb.PRDAction;

import java.util.Objects;

public abstract class Action {
    protected String type;
    protected SecondaryEntity secondaryEntity;
    protected String entityName;
    public Action(PRDAction action) {
        type = action.getType();
        entityName = Objects.isNull(action.getEntity()) ? "" : action.getEntity();

        if (!Objects.isNull(action.getPRDSecondaryEntity()))
            this.secondaryEntity = new SecondaryEntity(action.getPRDSecondaryEntity());
    }
    public String getType() { return type; }
    public SecondaryEntity getSecondaryEntity() { return secondaryEntity; }
    public String getEntityName() { return entityName; }
}