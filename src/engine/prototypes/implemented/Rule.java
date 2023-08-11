package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDRule;

import java.io.Serializable;
import java.util.Objects;

public class Rule implements Serializable {
    protected String name;
    protected Actions actions;
    protected Activation activation;

    public Rule(PRDRule rule) {
        if (!Objects.isNull(rule)) {
            name = rule.getName();
            actions = new Actions(rule.getPRDActions().getPRDAction());
            activation = new Activation(rule.getPRDActivation());
        }
    }

    public Actions getActions() {
        return actions;
    }

    public void setActions(Actions actions) {
        this.actions = actions;
    }

    public Activation getActivation() {
        return activation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
