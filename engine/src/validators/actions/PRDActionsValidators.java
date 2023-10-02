package validators.actions;

import prototypes.jaxb.PRDAction;
import prototypes.jaxb.PRDWorld;

import java.util.List;

public abstract class PRDActionsValidators {
    public static boolean validateActions(PRDWorld world, List<PRDAction> actions) throws Exception {
        for (PRDAction action : actions)
            PRDActionValidators.validateAction(world, action);

        return true;
    }
}