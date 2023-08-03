package engine.validators.actions;

import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDWorld;

import java.util.List;

public class PRDActionsValidators {
    public static boolean validateActions(PRDWorld world, List<PRDAction> actions) {
        for (PRDAction action : actions) {
            if (!PRDActionValidators.validateAction(world, action))
                return false;
        }

        return true;
    }
}
