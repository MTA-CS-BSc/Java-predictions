package engine.simulation.performers;

import engine.exceptions.EntityNotFoundException;
import engine.logs.EngineLoggers;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.KillAction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class KillPerformer {
    private static void handleAll(World world, KillAction action) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        mainEntity.setPopulation(0);
        mainEntity.setSingleEntities(Collections.emptyList());

        EngineLoggers.SIMULATION_LOGGER.info(String.format("Killed entity [%s]", action.getEntityName()));
    }
    public static void handleSingle(World world, String entityName, SingleEntity kill) {
        Entity mainEntity = Utils.findEntityByName(world, entityName);

        List<SingleEntity> updatedList = mainEntity.getSingleEntities()
                                        .stream()
                                        .filter(element -> !element.equals(kill))
                                        .collect(Collectors.toList());

        mainEntity.setSingleEntities(updatedList);
        mainEntity.setPopulation(mainEntity.getPopulation() - 1);

        EngineLoggers.SIMULATION_LOGGER.info(String.format("Killed 1 entity named [%s]. Population is [%d]", entityName, mainEntity.getPopulation()));
    }
    public static void handle(World world, KillAction action, SingleEntity kill) throws EntityNotFoundException {
        if (Objects.isNull(Utils.findEntityByName(world, action.getEntityName())))
            throw new EntityNotFoundException(String.format("Action [%s]: Entity [%s] does not exist", action.getType(), action.getEntityName()));

        if (Objects.isNull(kill))
            handleAll(world, action);

        else
            handleSingle(world, action.getEntityName(), kill);
    }
}
