package dtos;

import java.util.List;

public class WorldDTO {
    protected List<PropertyDTO> environment;
    protected List<RuleDTO> rules;
    protected TerminationDTO termination;
    protected List<EntityDTO> entities;
    protected int gridRows;
    protected int gridColumns;
    public WorldDTO(List<EntityDTO> entities, TerminationDTO termination,
                    List<RuleDTO> rules, List<PropertyDTO> environment, int gridRows, int gridColumns) {
        this.environment = environment;
        this.rules = rules;
        this.termination = termination;
        this.entities = entities;
        this.gridRows = gridRows;
        this.gridColumns = gridColumns;
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
    public List<PropertyDTO> getEnvironment() {
        return environment;
    }

    public List<RuleDTO> getRules() {
        return rules;
    }

    public TerminationDTO getTermination() {
        return termination;
    }

    public List<EntityDTO> getEntities() {
        return entities;
    }

    public int getGridRows() { return gridRows; }
    public int getGridColumns() { return gridColumns; }
}
