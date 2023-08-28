package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDAction;

public class Between {
    protected String sourceEntity;
    protected String targetEntity;

    public Between(PRDAction.PRDBetween _between) {
        sourceEntity = _between.getSourceEntity();
        targetEntity = _between.getTargetEntity();
    }
}
