package engine.simulation.performers;

import engine.consts.ReplaceModes;
import engine.modules.Utils;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;
import engine.prototypes.implemented.actions.ReplaceAction;

public abstract class ReplacePerformer {
    public static void performAction(World world, ReplaceAction action, SingleEntity singleKillEntity) {
        Entity killEntity = Utils.findEntityByName(world, action.getKill());
        Entity createEntity = Utils.findEntityByName(world, action.getCreate());
        String mode = action.getMode();

        SingleEntity created = mode.equals(ReplaceModes.SCRATCH) ?
                CreatePerformer.scratch(createEntity, world.getGrid()) : CreatePerformer.derived(createEntity, killEntity, singleKillEntity, world.getGrid());

        // Check if entity was killed, if it was - deliver the created entity
        if (KillPerformer.handle(world, killEntity.getName(), singleKillEntity)) {
            createEntity.getSingleEntities().add(created);
            createEntity.setPopulation(createEntity.getPopulation() + 1);
        }
    }
}
