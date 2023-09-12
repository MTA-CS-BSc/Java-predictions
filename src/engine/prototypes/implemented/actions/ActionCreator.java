package engine.prototypes.implemented.actions;

import helpers.types.ActionTypes;
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

    public static Action createAction(Action other) {
        if (other instanceof IncreaseAction)
            return new IncreaseAction((IncreaseAction)other);

        else if (other instanceof DecreaseAction)
            return new DecreaseAction((DecreaseAction)other);

        else if (other instanceof CalculationAction)
            return new CalculationAction((CalculationAction)other);

        else if (other instanceof SetAction)
            return new SetAction((SetAction)other);

        else if (other instanceof KillAction)
            return new KillAction((KillAction)other);

        else if (other instanceof ReplaceAction)
            return new ReplaceAction((ReplaceAction)other);

        else if (other instanceof ConditionAction)
            return new ConditionAction((ConditionAction)other);

        else if (other instanceof ProximityAction)
            return new ProximityAction((ProximityAction)other);

        return null;
    }
}