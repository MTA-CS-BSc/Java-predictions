package fx.mgmt.world.details.models.actions;

import fx.mgmt.world.details.models.TreeItemModel;

import java.util.Objects;

public class ActionModel extends TreeItemModel {
    private final String type;
    private final String entityName;
    private final boolean hasSecondaryEntity;
    private final SecondaryEntityModel secondaryEntity;

    public ActionModel(String type, String entityName, SecondaryEntityModel secondaryEntity) {
        super(type);
        this.type = type;
        this.entityName = entityName;
        this.hasSecondaryEntity = !Objects.isNull(secondaryEntity);
        this.secondaryEntity = secondaryEntity;
    }

    public String getType() {
        return type;
    }

    public String getEntityName() {
        return entityName;
    }

    public boolean isHasSecondaryEntity() {
        return hasSecondaryEntity;
    }

    public SecondaryEntityModel getSecondaryEntity() {
        return secondaryEntity;
    }
}
