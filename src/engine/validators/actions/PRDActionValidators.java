package engine.validators.actions;

import engine.modules.ActionTypes;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDWorld;
import engine.validators.Utils;

import java.util.Objects;

public class PRDActionValidators {
    public static boolean validateAction(PRDWorld world, PRDAction action) {
        if (action.getType().equals(ActionTypes.KILL))
            return validateKillAction(world, action);

        else if (action.getType().equals(ActionTypes.SET))
            System.out.println("Not implemented");
            //return validateSetAction(world, action);

        else if (action.getType().equals(ActionTypes.INCREASE)
        || action.getType().equals(ActionTypes.DECREASE))
            System.out.println("Not implemented");
            //return validateIncreaseDecreaseAction(world, action);

        else if (action.getType().equals(ActionTypes.CALCULATION))
            System.out.println("Not implemented");
            //return validateCalculationAction(world, action);

        else if (action.getType().equals(ActionTypes.CONDITION))
            System.out.println("Not implemented");
            //return validateConditionAction(world, action);

        return false;
    }

    public static boolean validateKillAction(PRDWorld world, PRDAction action) {
        return !Objects.isNull(Utils.findEntityByName(world, action.getEntity()));
    }
}