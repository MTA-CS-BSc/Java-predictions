package engine.prototypes.implemented;

import engine.consts.ActionTypes;
import engine.prototypes.implemented.actions.*;
import engine.prototypes.jaxb.PRDAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Actions implements Serializable {
    protected List<Action> actions = new ArrayList<>();
    public Actions(List<PRDAction> _actions) {
        _actions.forEach(action -> {
            Action created = createAction(action);

            if (!Objects.isNull(created))
                actions.add(createAction(action));
        });
    }
    private Action createAction(PRDAction action) {
        switch (action.getType()) {
            case ActionTypes.INCREASE:
                return new IncreaseAction(action);
            case ActionTypes.DECREASE:
                return new DecreaseAction(action);
            case ActionTypes.CALCULATION:
                return new CalculationAction(action);
            case ActionTypes.SET:
                return new SetAction(action);
            case ActionTypes.KILL:
                return new KillAction(action);
            case ActionTypes.REPLACE:
                return new ReplaceAction(action);
            case ActionTypes.CONDITION:
                return new ConditionAction(action);
            case ActionTypes.PROXIMITY:
                return new ProximityAction(action);
        }

        return null;
    }
    public List<Action> getActions() { return actions; }
}
