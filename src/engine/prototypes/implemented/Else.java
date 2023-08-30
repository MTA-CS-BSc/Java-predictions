package engine.prototypes.implemented;

import engine.prototypes.implemented.actions.Action;
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
            _else.getPRDAction().forEach(action -> actions.add(new Action(action)));
    }

    public List<Action> getActions() { return actions; }
}
