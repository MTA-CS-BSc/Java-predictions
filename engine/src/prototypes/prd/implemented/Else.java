package prototypes.prd.implemented;

import prototypes.prd.implemented.actions.Action;
import prototypes.prd.implemented.actions.ActionCreator;
import prototypes.prd.generated.PRDElse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Else implements Serializable {
    List<Action> actions;

    public Else(PRDElse _else) {
        actions = new ArrayList<>();

        if (!Objects.isNull(_else))
            _else.getPRDAction().forEach(action -> {
                Action created = ActionCreator.createAction(action);

                if (!Objects.isNull(created))
                    actions.add(created);
            });
    }

    public Else(Else other) {
        actions = new ArrayList<>();

        if (!Objects.isNull(other))
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
