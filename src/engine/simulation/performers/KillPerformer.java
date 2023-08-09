package engine.simulation.performers;

import engine.logs.EngineLoggers;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KillPerformer {
    public static void handleAll(World world, Action action) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        mainEntity.setPopulation(0);
        mainEntity.setSingleEntities(Collections.emptyList());

        EngineLoggers.SIMULATION_LOGGER.info(String.format("Killed entity [%s]", action.getEntityName()));
    }
    public static void handleSingle(World world, Action action, SingleEntity kill) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        List<SingleEntity> updatedList = mainEntity.getSingleEntities()
                                        .stream()
                                        .filter(element -> !element.equals(kill))
                                        .collect(Collectors.toList());

        mainEntity.setSingleEntities(updatedList);
        mainEntity.setPopulation(mainEntity.getPopulation() - 1);

        EngineLoggers.SIMULATION_LOGGER.info(String.format("Killed 1 entity named [%s]. Population is [%d]", action.getEntityName(), mainEntity.getPopulation()));
    }
    public static void handle(World world, Action action, SingleEntity kill) {
        if (Objects.isNull(kill))
            handleAll(world, action);

        else
            handleSingle(world, action, kill);
    }
}
