package dtos;

public class StopConditionDTO {
    protected String byWho;
    protected int count;

    public StopConditionDTO(String byWho, int count) {
        this.byWho = byWho;
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format("Stop after [%d] %s\n", count, byWho);
    }
}
