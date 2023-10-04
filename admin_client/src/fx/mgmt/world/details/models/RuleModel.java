package fx.mgmt.world.details.models;

import fx.mgmt.world.details.models.actions.ActionModel;

import java.util.List;

public class RuleModel extends TreeItemModel {
    private final int ticks;
    private final double probability;
    private final List<ActionModel> actions;

    public RuleModel(String name, int ticks, double probability, List<ActionModel> actions) {
        super(name);
        this.ticks = ticks;
        this.probability = probability;
        this.actions = actions;
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }

    public List<ActionModel> getActions() {
        return actions;
    }
}
