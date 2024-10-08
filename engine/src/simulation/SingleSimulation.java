package simulation;

import consts.TerminationReasons;
import modules.RandomGenerator;
import modules.Utils;
import prototypes.SingleEntity;
import prototypes.prd.implemented.*;
import prototypes.prd.implemented.actions.Action;
import simulation.performers.ActionsPerformer;
import modules.ElapsedTimer;
import types.ByStep;
import types.Coordinate;
import types.Direction;
import types.SimulationState;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class SingleSimulation extends SingleSimulationLog implements Serializable {
    protected SimulationState simulationState;
    protected World world;
    protected long ticks;
    protected String uuid;
    protected ElapsedTimer elapsedTimer;
    protected ByStep byStep;
    protected String createdUser;
    protected String requestUuid;

    public SingleSimulation(String createdUser, String requestUuid) {
        uuid = UUID.randomUUID().toString();
        ticks = 0;
        elapsedTimer = new ElapsedTimer();
        byStep = ByStep.NOT_BY_STEP;
        simulationState = SimulationState.CREATED;
        this.createdUser = createdUser;
        this.requestUuid = requestUuid;
        setCreatedTime(new Date());
    }

    public SingleSimulation(String createdUser, String requestUuid, World world) {
        this(createdUser, requestUuid);
        this.world = world;
    }

    public SingleSimulation(SingleSimulation other) {
        this(other.createdUser, other.requestUuid);
        this.world = new World(other.world, other.getStartWorldState());
    }

    public String getUUID() {
        return uuid;
    }

    public String isSimulationFinished(long startTimeMillis) {
        Termination termination = world.getTermination();

        for (Object stopCondition : termination.getStopConditions()) {
            if (stopCondition.getClass() == BySecond.class
                    && System.currentTimeMillis() - startTimeMillis >= (long) ((BySecond) stopCondition).getCount() * 1000) {
                simulationState = SimulationState.FINISHED;
                return TerminationReasons.BY_SECOND;
            } else if (stopCondition.getClass() == ByTicks.class
                    && ticks >= ((ByTicks) stopCondition).getCount()) {
                simulationState = SimulationState.FINISHED;
                return TerminationReasons.BY_TICKS;
            }
        }

        return simulationState == SimulationState.FINISHED ? "ByUser" : "";
    }

    public ByStep getByStep() {
        return byStep;
    }

    public void setByStep(ByStep value) {
        byStep = value;
    }

    private void moveEntity(SingleEntity singleEntity) {
        Direction randomDirection = Direction.values()[RandomGenerator.randomizeRandomNumber(0, Direction.values().length - 1)];
        Coordinate newCoordinate = new Coordinate(singleEntity.getCoordinate());

        switch (randomDirection) {
            case UP:
                newCoordinate.setX(newCoordinate.getX() - 1);

                if (newCoordinate.getX() < 0)
                    newCoordinate.setX(world.getGrid().getRows() - 1);
                break;
            case DOWN:
                newCoordinate.setX(newCoordinate.getX() + 1);

                if (newCoordinate.getX() > world.getGrid().getRows() - 1)
                    newCoordinate.setX(0);
                break;
            case LEFT:
                newCoordinate.setY(newCoordinate.getY() - 1);

                if (newCoordinate.getY() < 0)
                    newCoordinate.setY(world.getGrid().getColumns() - 1);
                break;
            case RIGHT:
                newCoordinate.setY(newCoordinate.getY() + 1);

                if (newCoordinate.getY() > world.getGrid().getColumns() - 1)
                    newCoordinate.setY(0);
                break;
        }

        if (RandomGenerator.isCoordinateTaken(world.getGrid(), newCoordinate))
            return;

        world.getGrid().changeCoordinateState(singleEntity.getCoordinate());
        world.getGrid().changeCoordinateState(newCoordinate);
        singleEntity.setCoordinate(newCoordinate);
    }

    private void performAllPossibleActions() {
        List<Action> actionsToPerform = getActionsToPerform();
        List<SingleEntity> allEntities = getAllSingleEntities();

        allEntities.forEach(singleEntity -> {
            actionsToPerform.forEach(action -> {
                if (!action.getEntityName().isEmpty() && !action.getEntityName().equals(singleEntity.getEntityName())) {
                    // Skip action
                } else if (action.getEntityName().isEmpty() || action.getEntityName().equals(singleEntity.getEntityName())) {
                    try {
                        ActionsPerformer.fireAction(world, action, singleEntity);
                    } catch (Exception e) {
                        simulationState = SimulationState.ERROR;
                    }
                }
            });

            moveEntity(singleEntity);
        });

        KillReplaceSaver.storage.forEach(Runnable::run);
        KillReplaceSaver.storage.clear();
    }

    public void handleSingleTick() {
        if (byStep != ByStep.PAST) {
            ticks++;
            ActionsPerformer.updateStableTimeToAllProps(world);
            performAllPossibleActions();
            ActionsPerformer.updateChangesTickToAll(world, ticks);
            pushWorldState(world);
        } else {
            if (ticks == 0)
                return;

            ticks--;
            world.setByWorldState(popWorldState());
        }
    }

    private List<SingleEntity> getAllSingleEntities() {
        return world.getEntities().getEntitiesMap().values()
                .stream()
                .map(Entity::getSingleEntities)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private List<Action> getActionsToPerform() {
        return Utils.getRulesToApply(world, ticks).values()
                .stream()
                .map(Rule::getActions)
                .map(Actions::getActions)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public void initializeRandomVariables() {
        world.initAllRandomVars();
    }

    public void run() {
        if (Objects.isNull(world) || simulationState == SimulationState.ERROR)
            return;

        if (simulationState == SimulationState.CREATED) {
            initializeRandomVariables();
            enqueueWorldState(world);
            elapsedTimer.start();
        }

        setStartTime(new Date());
        simulationState = SimulationState.RUNNING;

        while (isSimulationFinished(elapsedTimer.getElapsedTime()).isEmpty()
                && Arrays.asList(SimulationState.PAUSED, SimulationState.RUNNING).contains(simulationState)) {
            if (simulationState == SimulationState.RUNNING) {
                if (!elapsedTimer.isRunning())
                    elapsedTimer.resume();

                handleSingleTick();

                if (byStep != ByStep.NOT_BY_STEP)
                    simulationState = SimulationState.PAUSED;
            } else if (elapsedTimer.isRunning())
                elapsedTimer.pause();
        }

        if (elapsedTimer.isRunning())
            elapsedTimer.pause();

        if (simulationState == SimulationState.FINISHED)
            setEndTime(new Date());
    }

    public World getWorld() {
        return world;
    }

    public SimulationState getSimulationState() {
        return simulationState;
    }

    public void setSimulationState(SimulationState value) {
        simulationState = value;
    }

    public int getOverallPopulation() {
        return world.getEntities()
                .getEntitiesMap()
                .values()
                .stream()
                .mapToInt(Entity::getPopulation)
                .sum();
    }

    public WorldGrid getGrid() {
        return world.getGrid();
    }

    public int getMaxEntitiesAmount() {
        return world.getGrid().getColumns() * world.getGrid().getRows();
    }

    public long getTicks() {
        return ticks;
    }

    public long getElapsedTime() {
        return elapsedTimer.getElapsedTime();
    }

    public int getEntityAmountForTick(String entityName, int tick) {
        if (Objects.isNull(worldStatesByTicks.get(tick)))
            return 0;

        return worldStatesByTicks.get(tick).getEntitiesMap().get(entityName).getPopulation();
    }

    public String getRequestUuid() {
        return requestUuid;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    @Override
    public String toString() {
        return "--------------------------------------\n" +
                "----------Simulation details-----------\n" +
                "--------------------------------------\n" +
                world.toString();
    }
}
