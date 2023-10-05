package prototypes.prd.implemented.actions;

import prototypes.prd.implemented.SecondaryEntity;
import prototypes.prd.generated.PRDAction;

import java.util.Objects;

public abstract class Action {
    protected String type;
    protected SecondaryEntity secondaryEntity;
    protected String entityName;

    public Action(PRDAction action) {
        type = action.getType();
        entityName = Objects.isNull(action.getEntity()) ? "" : action.getEntity();

        if (!Objects.isNull(action.getPRDSecondaryEntity()))
            secondaryEntity = new SecondaryEntity(action.getPRDSecondaryEntity());
    }

    public Action(Action other) {
        type = other.getType();
        entityName = other.getEntityName();

        if (!Objects.isNull(other.getSecondaryEntity()))
            secondaryEntity = new SecondaryEntity(other.getSecondaryEntity());
    }

    public String getType() {
        return type;
    }

    public SecondaryEntity getSecondaryEntity() {
        return secondaryEntity;
    }

    public String getEntityName() {
        return entityName;
    }
}