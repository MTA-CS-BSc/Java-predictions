package engine.simulation.performers;

import engine.logs.Loggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KillPerformer {
    public static void handleAll(World world, String entityName) {
        world.getEntities().getEntitiesMap().remove(entityName);
    }
    public static void handleSingle(Entity mainEntity, SingleEntity kill) {
        List<SingleEntity> updatedList = mainEntity.getSingleEntities()
                                        .stream()
                                        .filter(element -> !element.equals(kill))
                                        .collect(Collectors.toList());

        mainEntity.setSingleEntities(updatedList);
        mainEntity.setPopulation(mainEntity.getPopulation() - 1);
    }
    public static void handle(World world, Action action, SingleEntity kill) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        if (Objects.isNull(kill))
            handleAll(world, action.getEntityName());

        else
            handleSingle(mainEntity, kill);

        Loggers.SIMULATION_LOGGER.info("Kill evaluate");
    }
}
