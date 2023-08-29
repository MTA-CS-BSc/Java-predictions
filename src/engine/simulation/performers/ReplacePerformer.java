package engine.simulation.performers;

import engine.consts.ReplaceModes;
import engine.modules.Utils;
import engine.prototypes.implemented.Action;
import engine.prototypes.implemented.Entity;
import engine.prototypes.implemented.SingleEntity;
import engine.prototypes.implemented.World;

import java.util.Objects;

public abstract class ReplacePerformer {
    public static void handleAll(World world, Action action) {
        for (SingleEntity singleKillEntity : Utils.findEntityByName(world, action.getKill()).getSingleEntities())
            handleSingle(world, action, singleKillEntity);
    }
    public static void handleSingle(World world, Action action, SingleEntity singleKillEntity) {
        Entity killEntity = Utils.findEntityByName(world, action.getKill());
        Entity createEntity = Utils.findEntityByName(world, action.getCreate());
        String mode = action.getMode();

        SingleEntity created = mode.equals(ReplaceModes.SCRATCH) ?
                CreatePerformer.scratch(createEntity) : CreatePerformer.derived(createEntity, killEntity, singleKillEntity);
        KillPerformer.handleSingle(world, killEntity.getName(), singleKillEntity);

        createEntity.getSingleEntities().add(created);
        createEntity.setPopulation(createEntity.getPopulation() + 1);
    }
    public static void handle(World world, Action action, SingleEntity on) throws Exception {
        if (Objects.isNull(on))
            handleAll(world, action);

        else
            handleSingle(world, action, on);
    }
}
