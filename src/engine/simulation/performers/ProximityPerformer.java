package engine.simulation.performers;

import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;
import engine.prototypes.implemented.WorldGrid;
import engine.prototypes.implemented.actions.Action;
import engine.prototypes.implemented.actions.ProximityAction;

import java.util.Objects;

public abstract class ProximityPerformer {
    private static boolean isInDepth(WorldGrid grid, SingleEntity ent1, SingleEntity ent2, int depth) {
        //TODO: Not implemented
        return true;
    }

    private static void handleSingleMainSingleSecondary(World world, ProximityAction action,
                                                        SingleEntity main, SingleEntity secondary) throws Exception {
        int depth = Integer.parseInt(ExpressionParser.evaluateExpression(world, action.getDepthExpression(), main));

        if (isInDepth(world.getGrid(), main, secondary, depth))
            for (Action actToPerform : action.getActions().getActions())
                ActionsPerformer.fireAction(world, actToPerform, main);
    }
    public static void performAction(World world, ProximityAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        Entity targetEntity = Utils.findEntityByName(world, action.getBetween().getTargetEntity());

        if (!Objects.isNull(secondary) && action.getBetween().getTargetEntity().equals(secondary.getEntityName()))
            handleSingleMainSingleSecondary(world, action, main, secondary);

        else
            for (SingleEntity singleTargetEntity : targetEntity.getSingleEntities())
                handleSingleMainSingleSecondary(world, action, main, singleTargetEntity);
    }
}
