package prototypes.implemented;

import prototypes.jaxb.PRDAction;

public class SecondaryEntity {
    protected Selection selection;
    protected String entityName;

    public SecondaryEntity(PRDAction.PRDSecondaryEntity secondaryEntity) {
        selection = new Selection(secondaryEntity.getPRDSelection());
        entityName = secondaryEntity.getEntity();
    }

    public SecondaryEntity(SecondaryEntity other) {
        entityName = other.getEntityName();
        selection = new Selection(other.getSelection());
    }

    public String getEntityName() {
        return entityName;
    }

    public Selection getSelection() {
        return selection;
    }
}
