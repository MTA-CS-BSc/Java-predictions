package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDAction;

import java.util.ArrayList;
import java.util.List;

public class Actions {
    protected List<Action> actions = new ArrayList<>();

    public Actions(List<PRDAction> _actions) {
        _actions.forEach(action -> actions.add(new Action(action)));
    }

    public List<Action> getActions() { return actions; }
}
