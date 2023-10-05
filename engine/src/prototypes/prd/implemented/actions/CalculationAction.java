package prototypes.prd.implemented.actions;

import prototypes.prd.implemented.Divide;
import prototypes.prd.implemented.Multiply;
import prototypes.prd.generated.PRDAction;
import types.CalculationTypes;

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

    public CalculationAction(CalculationAction other) {
        super(other);

        resultPropertyName = other.getResultPropertyName();

        if (!Objects.isNull(other.getMultiply())) {
            multiply = new Multiply(other.getMultiply());
            operationType = CalculationTypes.MULTIPLY;
        }

        if (!Objects.isNull(other.getDivide())) {
            divide = new Divide(other.getDivide());
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

    public String getOperationType() {
        return operationType;
    }
}
