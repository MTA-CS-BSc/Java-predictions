package dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dtos.actions.ActionDTO;

import java.util.List;

public class RuleDTO {
    protected String name;
    protected int ticks;
    protected double probability;
    protected List<ActionDTO> actions;

    @JsonCreator
    public RuleDTO(@JsonProperty("name") String name,
                   @JsonProperty("ticks") int ticks,
                   @JsonProperty("probability") double probability,
                   @JsonProperty("actions") List<ActionDTO> actions) {
        this.name = name;
        this.ticks = ticks;
        this.probability = probability;
        this.actions = actions;
    }
    public List<ActionDTO> getActions() {
        return actions;
    }
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
}
