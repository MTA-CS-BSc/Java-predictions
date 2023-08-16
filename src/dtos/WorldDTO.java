package dtos;

import java.util.List;

public class WorldDTO {
    protected List<PropertyDTO> environment;
    protected List<RuleDTO> rules;
    protected List<StopConditionDTO> termination;
    protected List<EntityDTO> entities;
    public WorldDTO(List<EntityDTO> _entities, List<StopConditionDTO> _termination,
                    List<RuleDTO> _rules, List<PropertyDTO> _environment) {
        environment = _environment;
        rules = _rules;
        termination = _termination;
        entities = _entities;
    }

    @Override
    public String toString() {
        //todo: implement to strings
        return entities.toString() + rules.toString() + termination.toString();
    }
}
