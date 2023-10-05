package simulation.performers;

import consts.ReplaceModes;
import modules.Utils;
import prototypes.prd.implemented.Entity;
import prototypes.SingleEntity;
import prototypes.prd.implemented.World;
import prototypes.prd.implemented.actions.ReplaceAction;

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
            world.getGrid().changeCoordinateState(created.getCoordinate());
        }
    }
}
