package prototypes.implemented;

import prototypes.jaxb.PRDCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Condition implements Serializable {
    protected List<Condition> conditions;
    protected String value;
    protected String singularity;
    protected String operator;
    protected String property;
    protected String logicalOperator;
    protected String entityName;

    public Condition(PRDCondition condition) {
        conditions = new ArrayList<>();
        value = condition.getValue();
        singularity = condition.getSingularity();
        operator = condition.getOperator();
        property = condition.getProperty();
        logicalOperator = condition.getLogical();
        entityName = condition.getEntity();

        condition.getPRDCondition().forEach(prdCondition -> conditions.add(new Condition(prdCondition)));
    }

    public Condition(Condition other) {
        conditions = new ArrayList<>();
        value = other.getValue();
        singularity = other.getSingularity();
        operator = other.getOperator();
        property = other.getProperty();
        logicalOperator = other.getLogicalOperator();
        entityName = other.getEntityName();

        other.getConditions().forEach(prdCondition -> conditions.add(new Condition(prdCondition)));
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public String getValue() {
        return value;
    }

    public String getSingularity() {
        return singularity;
    }

    public String getOperator() {
        return operator;
    }

    public String getProperty() {
        return property;
    }

    public String getLogicalOperator() {
        return logicalOperator;
    }

    public String getEntityName() {
        return entityName;
    }

}
