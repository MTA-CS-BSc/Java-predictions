package prototypes.prd.implemented;

import prototypes.prd.implemented.actions.Action;
import prototypes.prd.implemented.actions.ActionCreator;
import prototypes.prd.generated.PRDAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Actions implements Serializable {
    protected List<Action> actions;

    public Actions(List<PRDAction> actions) {
        this.actions = new ArrayList<>();

        actions.forEach(action -> {
            Action created = ActionCreator.createAction(action);

            if (!Objects.isNull(created))
                this.actions.add(created);
        });
    }

    public Actions(Actions other) {
        this.actions = new ArrayList<>();

        other.getActions().forEach(action -> {
            Action created = ActionCreator.createAction(action);

            if (!Objects.isNull(created))
                this.actions.add(created);
        });
    }

    public List<Action> getActions() {
        return actions;
    }
}
