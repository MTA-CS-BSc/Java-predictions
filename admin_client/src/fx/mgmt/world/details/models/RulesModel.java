package fx.mgmt.world.details.models;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.List;

public class RulesModel extends TreeItemModel {
    private final List<RuleModel> rules;

    public RulesModel(List<RuleModel> rules) {
        super(StringUtils.capitalize(WorldTreeViewCategories.RULES.name().toLowerCase()));
        this.rules = rules;
    }

    public List<RuleModel> getRules() {
        return rules;
    }
}
