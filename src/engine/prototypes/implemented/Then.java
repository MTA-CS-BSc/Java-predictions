package engine.prototypes.implemented;

import engine.prototypes.implemented.actions.Action;
import engine.prototypes.jaxb.PRDThen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Then implements Serializable {
    List<Action> actions;

    public Then(PRDThen then) {
        actions = new ArrayList<>();

        if (!Objects.isNull(then))
            then.getPRDAction().forEach(action -> actions.add(new Action(action)));
    }

    public List<Action> getActions() { return actions; }
}
