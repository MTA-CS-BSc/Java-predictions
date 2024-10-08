package simulation.performers;

import logs.EngineLoggers;
import modules.Utils;
import prototypes.prd.implemented.Entity;
import prototypes.SingleEntity;
import prototypes.prd.implemented.World;
import prototypes.prd.implemented.actions.KillAction;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class KillPerformer {
    // Returns true if the entity was killed, false otherwise (might be dead already due to earlier action from queue)
    public static boolean handle(World world, String entityName, SingleEntity kill) {
        Entity mainEntity = Utils.findEntityByName(world, entityName);

        if (Objects.isNull(mainEntity) || mainEntity.getPopulation() == 0)
            return false;

        List<SingleEntity> updatedList = mainEntity.getSingleEntities()
                .stream()
                .filter(element -> !element.equals(kill))
                .collect(Collectors.toList());

        if (mainEntity.getSingleEntities().size() > updatedList.size()) {
            mainEntity.setSingleEntities(updatedList);
            mainEntity.setPopulation(mainEntity.getPopulation() - 1);
            world.getGrid().changeCoordinateState(kill.getCoordinate());

            EngineLoggers.SIMULATION_LOGGER.info(String.format("Killed 1 entity named [%s]. Population is [%d]", entityName, mainEntity.getPopulation()));
            return true;
        }

        return false;
    }

    public static void performAction(World world, KillAction action, SingleEntity main, SingleEntity secondary) {
        handle(world, action.getEntityName(), action.getEntityName().equals(main.getEntityName()) ? main : secondary);
    }
}
