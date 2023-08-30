package engine.prototypes.implemented;

import engine.prototypes.implemented.actions.Action;
import engine.prototypes.implemented.actions.ActionCreator;
import engine.prototypes.jaxb.PRDAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Actions implements Serializable {
    protected List<Action> actions = new ArrayList<>();
    public Actions(List<PRDAction> _actions) {
        _actions.forEach(action -> {
            Action created = ActionCreator.createAction(action);

            if (!Objects.isNull(created))
                actions.add(created);
        });
    }
    public List<Action> getActions() { return actions; }
}
