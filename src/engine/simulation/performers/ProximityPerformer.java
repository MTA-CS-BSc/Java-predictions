package engine.simulation.performers;

import engine.modules.Utils;
import engine.parsers.ExpressionParser;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.Action;
import engine.prototypes.implemented.actions.ProximityAction;
import helpers.types.Bounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class ProximityPerformer {
    private static Bounds getBounds(WorldGrid grid, Coordinate start, int depth) {
        Bounds bounds = new Bounds();

        int mRowIterator = start.getX();
        int pRowIterator = start.getX();
        int mColIterator = start.getY();
        int pColIterator = start.getY();

        int amount = 0;

        while (amount < depth) {
            mRowIterator--;
            mColIterator--;
            pRowIterator++;
            pColIterator++;

            if (mRowIterator < 0)
                mRowIterator = grid.getRows() - 1;

            if (mColIterator < 0)
                mColIterator = grid.getColumns() - 1;

            if (pRowIterator > grid.getRows() - 1)
                pRowIterator = 0;

            if (pColIterator > grid.getColumns() - 1)
                pColIterator = 0;

            amount++;
        }

        bounds.setRowMDepth(mRowIterator);
        bounds.setColMDepth(mColIterator);
        bounds.setRowPDepth(pRowIterator);
        bounds.setColPDepth(pColIterator);

        return bounds;
    }

    private static List<Coordinate> getDepthCoordinates(WorldGrid grid, Coordinate start, int depth) {
        List<Coordinate> relevant = new ArrayList<>();
        Bounds bounds = getBounds(grid, start, depth);

        int iterator = bounds.colMDepth;

        while (iterator != (bounds.colPDepth + 1) % grid.getColumns()) {
            relevant.add(new Coordinate(bounds.rowMDepth, iterator));
            relevant.add(new Coordinate(bounds.rowPDepth, iterator));

            iterator++;

            if (iterator == grid.getColumns())
                iterator = 0;
        }

        iterator = bounds.rowMDepth;
        while (iterator != (bounds.rowPDepth + 1) % grid.getRows()) {
            relevant.add(new Coordinate(iterator, bounds.colMDepth));
            relevant.add(new Coordinate(iterator, bounds.colPDepth));

            iterator++;

            if (iterator == grid.getRows())
                iterator = 0;
        }

        return relevant;
    }

    private static boolean isInDepth(WorldGrid grid, SingleEntity ent1, SingleEntity ent2, int depth) {
        return getDepthCoordinates(grid, ent1.getCoordinate(), depth)
                .stream()
                .anyMatch(coordinate -> coordinate.equals(ent2.getCoordinate()));
    }

    private static void handleSingleMainSingleSecondary(World world, ProximityAction action,
                                                        SingleEntity main, SingleEntity secondary) throws Exception {
        int depth = Integer.parseInt(ExpressionParser.evaluateExpression(world, action.getDepthExpression(), main, secondary));

        if (isInDepth(world.getGrid(), main, secondary, depth))
            for (Action actToPerform : action.getActions().getActions())
                ActionsPerformer.handleAction(world, actToPerform, main, secondary);
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
