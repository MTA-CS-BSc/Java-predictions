package dtos;

import java.util.List;

public class WorldDTO {
    protected List<PropertyDTO> environment;
    protected List<RuleDTO> rules;
    protected TerminationDTO termination;
    protected List<EntityDTO> entities;
    public WorldDTO(List<EntityDTO> _entities, TerminationDTO _termination,
                    List<RuleDTO> _rules, List<PropertyDTO> _environment) {
        environment = _environment;
        rules = _rules;
        termination = _termination;
        entities = _entities;
    }
    @Override
    public String toString() {
        return getEntitiesDetails() + getRulesDetails() + getTerminationDetails();
    }
    public String getEntitiesDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("###########Entities###########\n");
        entities.forEach(entity -> sb.append(entity.toString()).append("\n"));
        return sb.toString();
    }
    public String getRulesDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("###########Rules###########\n");

        rules.forEach(rule -> sb.append(rule.toString()).append("\n"));

        return sb.toString();
    }
    public String getTerminationDetails() {
        return termination.toString();
    }
}
