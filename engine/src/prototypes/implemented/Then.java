package prototypes.implemented;

import prototypes.implemented.actions.Action;
import prototypes.implemented.actions.ActionCreator;
import prototypes.jaxb.PRDThen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Then implements Serializable {
    List<Action> actions;

    public Then(PRDThen then) {
        actions = new ArrayList<>();

        then.getPRDAction().forEach(action -> {
            Action created = ActionCreator.createAction(action);

            if (!Objects.isNull(created))
                actions.add(created);
        });
    }

    public Then(Then other) {
        actions = new ArrayList<>();

        other.getActions().forEach(action -> {
            Action created = ActionCreator.createAction(action);

            if (!Objects.isNull(created))
                actions.add(created);
        });
    }

    public List<Action> getActions() {
        return actions;
    }
}
