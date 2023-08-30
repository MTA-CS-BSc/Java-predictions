package engine.prototypes.implemented.actions;

import engine.prototypes.implemented.Divide;
import engine.prototypes.implemented.Multiply;
import engine.prototypes.jaxb.PRDAction;

import java.util.Objects;

public class CalculationAction extends Action {
    protected String entityName;
    protected Multiply multiply;
    protected Divide divide;
    protected String resultPropertyName;
    public CalculationAction(PRDAction action) {
        super(action.getType(), action.getPRDSecondaryEntity());

        entityName = action.getEntity();
        resultPropertyName = action.getResultProp();

        if (!Objects.isNull(action.getPRDMultiply()))
            multiply = new Multiply(action.getPRDMultiply());

        if (!Objects.isNull(action.getPRDDivide()))
            divide = new Divide(action.getPRDDivide());
    }
    public String getEntityName() {
        return entityName;
    }
    public Multiply getMultiply() {
        return multiply;
    }
    public Divide getDivide() {
        return divide;
    }
    public String getResultPropertyName() {
        return resultPropertyName;
    }
}
