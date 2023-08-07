package engine.validators.actions;

import engine.exceptions.EntityNotFoundException;
import engine.exceptions.InvalidTypeException;
import engine.exceptions.PRDThenNotFoundException;
import engine.exceptions.PropertyNotFoundException;
import engine.prototypes.jaxb.PRDAction;
import engine.prototypes.jaxb.PRDWorld;

import java.util.List;

public class PRDActionsValidators {
    public static boolean validateActions(PRDWorld world, List<PRDAction> actions) throws PropertyNotFoundException, PRDThenNotFoundException, EntityNotFoundException, InvalidTypeException {
        for (PRDAction action : actions)
            PRDActionValidators.validateAction(world, action);

        return true;
    }
}
