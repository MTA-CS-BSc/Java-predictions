package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Condition implements Serializable {
    protected List<Condition> conditions;
    protected String value;
    protected String singularity;
    protected String operator;
    protected String property;
    protected String logicalOperator;
    protected String entityName;

    public Condition(PRDCondition _condition) {
        if (!Objects.isNull(_condition)) {
            conditions = new ArrayList<>();
            value = _condition.getValue();
            singularity = _condition.getSingularity();
            operator = _condition.getOperator();
            property = _condition.getProperty();
            logicalOperator = _condition.getLogical();
            entityName = _condition.getEntity();

            _condition.getPRDCondition().forEach(prdCondition -> conditions.add(new Condition(prdCondition)));
        }

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
