package engine.simulation.performers;
import engine.consts.*;
import engine.modules.Utils;
import engine.prototypes.implemented.*;

import java.util.Objects;

public class ActionsPerformer {
    public static void fireAction(World world, Action action, SingleEntity on) {
        if (Objects.isNull(Utils.findEntityByName(world, action.getEntityName())))
            return;

        String type = action.getType();

        if (type.equalsIgnoreCase(ActionTypes.INCREASE))
            IncrementPerformer.handle(world, action, on);

        else if (type.equalsIgnoreCase(ActionTypes.DECREASE))
            DecrementPerformer.handle(world, action, on);

        else if (type.equalsIgnoreCase(ActionTypes.CALCULATION))
            CalculationPerformer.handle(world, action, on);

        else if (type.equalsIgnoreCase(ActionTypes.SET))
            SetPerformer.handle(world, action, on);

        else if (type.equalsIgnoreCase(ActionTypes.KILL))
            KillPerformer.handle(world, action, on);

        else if (type.equalsIgnoreCase(ActionTypes.CONDITION))
            ConditionPerformer.handle(world, action, on);
    }
}
