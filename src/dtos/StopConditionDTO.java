package dtos;

public class StopConditionDTO {
    protected String byWho;
    protected int count;

    public StopConditionDTO(String _byWho, int _count) {
        byWho = _byWho;
        count = _count;
    }
}
