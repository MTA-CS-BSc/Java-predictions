package engine.prototypes.implemented.actions;

import engine.consts.ActionTypes;
import engine.prototypes.jaxb.PRDAction;

public abstract class ActionCreator {
    public static Action createAction(PRDAction action) {
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
}
