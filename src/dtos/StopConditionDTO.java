package dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StopConditionDTO {
    protected String byWho;

    protected int count;

    @JsonCreator
    public StopConditionDTO(@JsonProperty("byWho") String byWho,
                            @JsonProperty("count") int count) {
        this.byWho = byWho;
        this.count = count;
    }

    public String getByWho() {
        return byWho;
    }

    public int getCount() {
        return count;
    }
}
