package engine.prototypes.implemented;

import engine.prototypes.implemented.actions.Action;
import engine.prototypes.implemented.actions.ActionCreator;
import engine.prototypes.jaxb.PRDElse;

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

    public List<Action> getActions() { return actions; }
}
