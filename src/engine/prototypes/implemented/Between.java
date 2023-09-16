package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDAction;

public class Between {
    protected String sourceEntity;
    protected String targetEntity;

    public Between(PRDAction.PRDBetween between) {
        sourceEntity = between.getSourceEntity();
        targetEntity = between.getTargetEntity();
    }

    public Between(Between other) {
        sourceEntity = other.getSourceEntity();
        targetEntity = other.getTargetEntity();
    }

    public String getSourceEntity() {
        return sourceEntity;
    }

    public String getTargetEntity() {
        return targetEntity;
    }
}
