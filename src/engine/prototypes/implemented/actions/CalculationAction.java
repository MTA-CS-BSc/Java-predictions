package engine.prototypes.implemented.actions;

import engine.consts.CalculationTypes;
import engine.prototypes.implemented.Divide;
import engine.prototypes.implemented.Multiply;
import engine.prototypes.jaxb.PRDAction;

import java.util.Objects;

public class CalculationAction extends Action {
    protected Multiply multiply;
    protected Divide divide;
    protected String resultPropertyName;
    protected String operationType;
    public CalculationAction(PRDAction action) {
        super(action);

        resultPropertyName = action.getResultProp();

        if (!Objects.isNull(action.getPRDMultiply())) {
            multiply = new Multiply(action.getPRDMultiply());
            operationType = CalculationTypes.MULTIPLY;
        }

        if (!Objects.isNull(action.getPRDDivide())) {
            divide = new Divide(action.getPRDDivide());
            operationType = CalculationTypes.DIVIDE;
        }
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
    public String getOperationType() { return operationType; }
}
