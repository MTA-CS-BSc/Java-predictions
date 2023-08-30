package engine.simulation.performers;

import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;
import engine.prototypes.implemented.WorldGrid;
import engine.prototypes.implemented.actions.Action;
import engine.prototypes.implemented.actions.ProximityAction;

public abstract class ProximityPerformer {
    private static boolean isInDepth(WorldGrid grid, SingleEntity ent1, SingleEntity ent2, int depth) {
        //TODO: Not implemented
        return true;
    }
    public static void performAction(World world, ProximityAction action, SingleEntity on) throws Exception {
        Entity targetEntity = Utils.findEntityByName(world, action.getBetween().getTargetEntity());
        int depth = Integer.parseInt(ExpressionParser.evaluateExpression(world, action, action.getDepthExpression(), on));

        for (SingleEntity singleTargetEntity : targetEntity.getSingleEntities())
            if (isInDepth(world.getGrid(), on, singleTargetEntity, depth))
                for (Action actToPerform : action.getActions().getActions())
                    ActionsPerformer.fireAction(world, actToPerform, on);
    }
}
