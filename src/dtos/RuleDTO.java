package dtos;

public class RuleDTO {
    protected String name;
    protected int ticks;
    protected double probability;
    protected int actionsAmount;
    public RuleDTO(String _name, int _ticks, double _probability, int _actionsAmount) {
        name = _name;
        ticks = _ticks;
        probability = _probability;
        actionsAmount = _actionsAmount;
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

    public int getActionsAmount() {
        return actionsAmount;
    }

    public void setActionsAmount(int actionsAmount) {
        this.actionsAmount = actionsAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#####Rule######\n");

        sb.append("Name: ").append(getName()).append("\n");
        sb.append("Activation ticks: ").append(getTicks()).append("\n");
        sb.append("Activation probability: ").append(getProbability()).append("\n");
        sb.append("Actions amount: ").append(getActionsAmount()).append("\n");

        return sb.toString();
    }
}
