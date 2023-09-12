package engine.simulation;

import engine.consts.TerminationReasons;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.Action;
import engine.simulation.performers.ActionsPerformer;
import helpers.modules.ElapsedTimer;
import helpers.types.SimulationState;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class SingleSimulation extends SingleSimulationLog implements Serializable {
    protected SimulationState simulationState;
    protected World world;
    protected long ticks = 0;
    protected String uuid;
    protected ElapsedTimer elapsedTimer;

    public SingleSimulation(World world) {
        elapsedTimer = new ElapsedTimer();
        uuid = UUID.randomUUID().toString();
        this.world = world;
        simulationState = SimulationState.CREATED;
    }
    public String getUUID() { return uuid; }
    public String isSimulationFinished(long startTimeMillis) {
        Termination termination = world.getTermination();

        for (Object stopCondition : termination.getStopConditions()) {
            if (stopCondition.getClass() == BySecond.class
                    && System.currentTimeMillis() - startTimeMillis >= (long)((BySecond)stopCondition).getCount() * 1000) {
                simulationState = SimulationState.FINISHED;
                return TerminationReasons.BY_SECOND;
            }

            else if (stopCondition.getClass() == ByTicks.class
                    && ticks >= ((ByTicks)stopCondition).getCount()) {
                simulationState = SimulationState.FINISHED;
                return TerminationReasons.BY_TICKS;
            }
        }

        return simulationState == SimulationState.FINISHED ? "ByUser" : "";
    }
    private void moveEntity(SingleEntity singleEntity) {
        //TODO: Not implemented
    }
    public void handleSingleTick() {
        List<Action> actionsToPerform = getActionsToPerform();
        List<SingleEntity> allEntities = getAllSingleEntities();

        allEntities.forEach(singleEntity -> {
           actionsToPerform.forEach(action -> {
              if (!action.getEntityName().isEmpty() && !action.getEntityName().equals(singleEntity.getEntityName())) {
                  // Skip action
              }

              else if (action.getEntityName().isEmpty() || action.getEntityName().equals(singleEntity.getEntityName())) {
                  try {
                      ActionsPerformer.fireAction(world, action, singleEntity);
                  } catch (Exception e) { simulationState = SimulationState.ERROR; }
              }
           });

           moveEntity(singleEntity);
        });

        KillReplaceSaver.storage.forEach(Runnable::run);
        KillReplaceSaver.storage.clear();
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
    public void run() throws Exception {
        if (Objects.isNull(world) || simulationState == SimulationState.ERROR)
            throw new Exception();

        if (simulationState == SimulationState.CREATED)
            setStartTime(new Date());

        elapsedTimer.startOrResume();
        simulationState = SimulationState.RUNNING;

        while (isSimulationFinished(elapsedTimer.getElapsedTime()).isEmpty()
                && Arrays.asList(SimulationState.STOPPED, SimulationState.RUNNING).contains(simulationState)) {
            if (simulationState == SimulationState.RUNNING) {
                if (!elapsedTimer.isRunning())
                    elapsedTimer.startOrResume();

                ticks++;
                handleSingleTick();
                ActionsPerformer.updateStableTimeToAllProps(world);
            }

            else if (elapsedTimer.isRunning())
                elapsedTimer.pause();
        }

        if (elapsedTimer.isRunning())
            elapsedTimer.pause();

        if (simulationState == SimulationState.FINISHED) {
            setEndTime(new Date());
            setFinishWorldState(world);
        }
    }
    public World getWorld() { return world; }
    public SimulationState getSimulationState() { return simulationState; }
    public void setSimulationState(SimulationState value) { simulationState = value; }
    @Override
    public String toString() {
        return "--------------------------------------\n" +
                "----------Simulation details-----------\n" +
                "--------------------------------------\n" +
                world.toString();
    }
}
