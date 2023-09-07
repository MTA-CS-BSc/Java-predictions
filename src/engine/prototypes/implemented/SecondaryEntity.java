package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDAction;

public class SecondaryEntity {
    protected Selection selection;
    protected String entityName;

    public SecondaryEntity(PRDAction.PRDSecondaryEntity secondaryEntity) {
        selection = new Selection(secondaryEntity.getPRDSelection());
        entityName = secondaryEntity.getEntity();
    }

    public String getEntityName() { return entityName; }
    public Selection getSelection() { return selection; }
}
