package engine.simulation.performers;

import engine.exceptions.EntityNotFoundException;
import engine.exceptions.ErrorMessageFormatter;
import engine.logs.Loggers;
import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KillPerformer {
    public static void handleAll(World world, Action action) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        mainEntity.setPopulation(0);
        mainEntity.setSingleEntities(Collections.emptyList());

        Loggers.SIMULATION_LOGGER.info(String.format("Killed entity [%s]", action.getEntityName()));
    }

    public static void handleSingle(World world, Action action, SingleEntity kill) {
        Entity mainEntity = Utils.findEntityByName(world, action.getEntityName());

        List<SingleEntity> updatedList = mainEntity.getSingleEntities()
                                        .stream()
                                        .filter(element -> !element.equals(kill))
                                        .collect(Collectors.toList());

        mainEntity.setSingleEntities(updatedList);
        mainEntity.setPopulation(mainEntity.getPopulation() - 1);

        Loggers.SIMULATION_LOGGER.info(String.format("Killed 1 entity named [%s]. Population is [%d]", action.getEntityName(), mainEntity.getPopulation()));
    }
    public static void handle(World world, Action action, SingleEntity kill) throws EntityNotFoundException {
        if (Objects.isNull(Utils.findEntityByName(world, action.getEntityName())))
            throw new EntityNotFoundException(ErrorMessageFormatter.formatEntityNotFoundMessage(action.getType(), action.getEntityName()));

        if (Objects.isNull(kill))
            handleAll(world, action);

        else
            handleSingle(world, action, kill);
    }
}
