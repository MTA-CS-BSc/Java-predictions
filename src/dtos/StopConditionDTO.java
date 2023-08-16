package dtos;

public class StopConditionDTO {
    protected String byWho;
    protected int count;

    public StopConditionDTO(String _byWho, int _count) {
        byWho = _byWho;
        count = _count;
    }

    @Override
    public String toString() {
        return String.format("Stop after [%d] %s\n", count, byWho);
    }
}
