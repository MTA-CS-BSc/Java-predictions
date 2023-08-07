package engine.validators.actions;

import engine.exceptions.EntityNotFoundException;
import engine.exceptions.InvalidTypeException;
import engine.exceptions.PropertyNotFoundException;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDWorld;

import java.util.List;

public class PRDActionsValidators {
    public static boolean validateActions(PRDWorld world, List<PRDAction> actions) throws Exception {
        for (PRDAction action : actions)
            PRDActionValidators.validateAction(world, action);

        return true;
    }
}
