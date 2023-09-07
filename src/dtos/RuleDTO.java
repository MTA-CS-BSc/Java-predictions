package dtos;

import dtos.actions.ActionDTO;

import java.util.List;
import java.util.Objects;

public class RuleDTO {
    protected String name;
    protected int ticks;
    protected double probability;
    protected List<ActionDTO> actions;
    public RuleDTO(String name, int ticks, double probability, List<ActionDTO> actions) {
        this.name = name;
        this.ticks = ticks;
        this.probability = probability;
        this.actions = actions;
    }
    public int getActionsAmount() { return !Objects.isNull(actions) ? actions.size() : 0; }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getTicks() {
        return ticks;
    }
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
    public double getProbability() {
        return probability;
    }
    public void setProbability(double probability) {
        this.probability = probability;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#####Rule######\n");

        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Activation ticks: ").append(getTicks()).append("\n");
        sb.append("Activation probability: ").append(getProbability()).append("\n");

        return sb.toString();
    }
}
