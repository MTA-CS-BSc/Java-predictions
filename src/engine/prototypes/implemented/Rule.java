package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDRule;

import java.io.Serializable;

public class Rule implements Serializable {
    protected String name;
    protected Actions actions;
    protected Activation activation;

    public Rule(PRDRule rule) {
        name = rule.getName();
        actions = new Actions(rule.getPRDActions().getPRDAction());
        activation = new Activation(rule.getPRDActivation());
    }

    public Rule(Rule other) {
        name = other.getName();
        actions = new Actions(other.getActions());
        activation = new Activation(other.getActivation());
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
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#####Rule######\n");

        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Activation ticks: ").append(getActivation().getTicks()).append("\n");
        sb.append("Activation probability: ").append(getActivation().getProbability()).append("\n");
        sb.append("Actions amount: ").append(getActions().getActions().size()).append("\n");

        return sb.toString();
    }
}
