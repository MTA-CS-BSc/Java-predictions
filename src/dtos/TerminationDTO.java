package dtos;

import java.util.List;

public class TerminationDTO {
    protected List<StopConditionDTO> stopConditions;
    protected boolean isByUser;

    public TerminationDTO(List<StopConditionDTO> stopConditions, boolean isByUser) {
        this.stopConditions = stopConditions;
        this.isByUser = isByUser;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("###########Termination###########\n");

        stopConditions.forEach(stopCondition -> {
            sb.append("#####Stop Condition######\n");
            sb.append(stopCondition);
        });

        if (isByUser) {
            sb.append("#####Stop Condition######\n");
            sb.append("Termination by user\n");
        }

        return sb.toString();
    }
}
