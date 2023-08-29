package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDAction;

import java.io.Serializable;
import java.util.Objects;

public class Action implements Serializable {
    protected Divide divide;
    protected Multiply multiply;
    protected Condition condition;
    protected Then _then;
    protected Else _else;
    protected String value;
    protected String type;
    protected String resultPropertyName;
    protected String propertyName;
    protected String entityName;
    protected String by;

    protected String kill;
    protected String create;
    protected String mode;
    protected String depthExpression;
    protected Between between;
    protected SecondaryEntity secondaryEntity;
    protected Actions actions;

    public Action(PRDAction action) {
        if (!Objects.isNull(action)) {
            value = action.getValue();
            type = action.getType();
            resultPropertyName = action.getResultProp();
            propertyName = action.getProperty();
            entityName = action.getEntity();
            by = action.getBy();
            create = action.getCreate();
            kill = action.getKill();
            mode = action.getMode();

            if (!Objects.isNull(action.getPRDActions()))
                actions = new Actions(action.getPRDActions().getPRDAction());

            if (!Objects.isNull(action.getPRDEnvDepth()))
                depthExpression = action.getPRDEnvDepth().getOf();

            if (!Objects.isNull(action.getPRDBetween()))
                between = new Between(action.getPRDBetween());

            if (!Objects.isNull(action.getPRDSecondaryEntity()))
                secondaryEntity = new SecondaryEntity(action.getPRDSecondaryEntity());

            if (!Objects.isNull(action.getPRDDivide()))
                divide = new Divide(action.getPRDDivide());

            if (!Objects.isNull(action.getPRDMultiply()))
                multiply = new Multiply(action.getPRDMultiply());

            _then = new Then(action.getPRDThen());
            _else = new Else(action.getPRDElse());
            condition = new Condition(action.getPRDCondition());
        }
    }

    public Divide getDivide() {
        return divide;
    }

    public Multiply getMultiply() {
        return multiply;
    }

    public Condition getCondition() {
        return condition;
    }

    public Then getThen() {
        return _then;
    }

    public Else getElse() {
        return _else;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String getResultPropertyName() {
        return resultPropertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getBy() {
        return by;
    }

    public Between getBetween() { return between; }
    public String getDepthExpression() { return depthExpression; }
    public Actions getActions() { return actions; }
    public String getCreate() { return create; }
    public String getMode() { return mode; }
    public String getKill() { return kill; }
}
