package engine.simulation.performers;

import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.Action;
import engine.prototypes.implemented.actions.ProximityAction;
import helpers.Bounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ProximityPerformer {
    private static Bounds getBounds(WorldGrid grid, Coordinate start, int depth) {
        Bounds bounds = new Bounds();
        bounds.setRowMDepth((start.getX() - depth) % grid.getRows());
        bounds.setRowPDepth((start.getX() + depth) % grid.getRows());
        bounds.setColMDepth((start.getY() - depth) % grid.getColumns());
        bounds.setColPDepth((start.getY() + depth) % grid.getColumns());

        return bounds;
    }
    private static List<Coordinate> getDepthCoordinates(WorldGrid grid, Coordinate start, int depth) {
        List<Coordinate> relevant = new ArrayList<>();
        Bounds bounds = getBounds(grid, start, depth);

        int iterator = bounds.colMDepth;
        do {
            relevant.add(new Coordinate(bounds.rowMDepth, iterator));
            relevant.add(new Coordinate(bounds.rowPDepth, iterator));

            iterator++;

            if (iterator == grid.getColumns())
                iterator = 0;
        } while (iterator != (bounds.colPDepth + 1) % grid.getColumns());

        iterator = bounds.rowMDepth;
        do {
            relevant.add(new Coordinate(iterator, bounds.colMDepth));
            relevant.add(new Coordinate(iterator, bounds.colPDepth));

            iterator++;

            if (iterator == grid.getRows())
                iterator = 0;
        } while (iterator != (bounds.rowPDepth + 1) % grid.getRows());

        return relevant;
    }
    private static boolean isInDepth(WorldGrid grid, SingleEntity ent1, SingleEntity ent2, int depth) {
        return getDepthCoordinates(grid, ent1.getCoordinate(), depth)
                .stream()
                .anyMatch(coordinate -> coordinate.equals(ent2.getCoordinate()));
    }
    private static void handleSingleMainSingleSecondary(World world, ProximityAction action,
                                                        SingleEntity main, SingleEntity secondary) throws Exception {
        int depth = Integer.parseInt(ExpressionParser.evaluateExpression(world, action.getDepthExpression(), main));

        if (isInDepth(world.getGrid(), main, secondary, depth))
            for (Action actToPerform : action.getActions().getActions())
                ActionsPerformer.fireAction(world, actToPerform, main);
    }
    public static void performAction(World world, ProximityAction action, SingleEntity main, SingleEntity secondary) throws Exception {
        Entity targetEntity = Utils.findEntityByName(world, action.getBetween().getTargetEntity());

        if (!Objects.isNull(secondary) && action.getBetween().getTargetEntity().equals(secondary.getEntityName()))
            handleSingleMainSingleSecondary(world, action, main, secondary);

        else
            for (SingleEntity singleTargetEntity : targetEntity.getSingleEntities())
                handleSingleMainSingleSecondary(world, action, main, singleTargetEntity);
    }
}
