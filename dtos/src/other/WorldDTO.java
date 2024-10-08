package other;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class WorldDTO {
    protected List<PropertyDTO> environment;
    protected List<RuleDTO> rules;
    protected TerminationDTO termination;
    protected List<EntityDTO> entities;
    protected int sleep;
    protected String name;
    protected int gridRows;
    protected int gridColumns;

    @JsonCreator
    public WorldDTO(@JsonProperty("entities") List<EntityDTO> entities,
                    @JsonProperty("termination") TerminationDTO termination,
                    @JsonProperty("rules") List<RuleDTO> rules,
                    @JsonProperty("environment") List<PropertyDTO> environment,
                    @JsonProperty("gridRows") int gridRows,
                    @JsonProperty("gridColumns") int gridColumns,
                    @JsonProperty("name") String name,
                    @JsonProperty("sleep") int sleep) {
        this.environment = environment;
        this.rules = rules;
        this.termination = termination;
        this.entities = entities;
        this.gridRows = gridRows;
        this.gridColumns = gridColumns;
        this.sleep = sleep;
        this.name = name;
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

    public int getGridRows() {
        return gridRows;
    }

    public int getGridColumns() {
        return gridColumns;
    }

    public String getName() { return name; }

    public int getSleep() { return sleep; }
}
