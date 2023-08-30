package engine.simulation.performers;

import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.Action;
import engine.prototypes.implemented.actions.ProximityAction;

import java.util.Objects;

public abstract class ProximityPerformer {
    private static boolean isInDepth(WorldGrid grid, SingleEntity ent1, SingleEntity ent2, int depth) {
        //TODO: Not implemented
        return true;
    }
    private static void handleAll(World world, ProximityAction action) throws Exception {
        Entity sourceEntity = Utils.findEntityByName(world, action.getBetween().getSourceEntity());

        for (SingleEntity entity : sourceEntity.getSingleEntities())
            handleSingle(world, action, entity);
    }
    private static void handleSingle(World world, ProximityAction action, SingleEntity on) throws Exception {
        Entity targetEntity = Utils.findEntityByName(world, action.getBetween().getTargetEntity());
        int depth = Integer.parseInt(ExpressionParser.evaluateExpression(world, action, action.getDepthExpression(), on));

        for (SingleEntity singleTargetEntity : targetEntity.getSingleEntities())
            if (isInDepth(world.getGrid(), on, singleTargetEntity, depth))
                for (Action actToPerform : action.getActions().getActions())
                    ActionsPerformer.fireAction(world, actToPerform, on);
    }
    public static void handle(World world, ProximityAction action, SingleEntity on) throws Exception {
        if (Objects.isNull(on))
            handleAll(world, action);

        else
            handleSingle(world, action, on);
    }
}
