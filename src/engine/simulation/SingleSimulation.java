package engine.simulation;

import engine.consts.TerminationReasons;
import engine.logs.EngineLoggers;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import engine.prototypes.implemented.actions.Action;
import engine.simulation.performers.ActionsPerformer;
import helpers.SimulationState;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class SingleSimulation extends SingleSimulationLog implements Serializable {
    protected SimulationState simulationState;
    protected World world;
    protected long ticks = 0;
    protected String uuid;

    public SingleSimulation(World _world) {
        uuid = UUID.randomUUID().toString();
        world = _world;
        simulationState = SimulationState.CREATED;
    }
    public String getUUID() { return uuid; }
    public String isSimulationFinished(long startTimeMillis) {
        //TODO: Add termination by user handle
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

        return "";
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
        });
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
    public void run() throws Exception {
        if (Objects.isNull(world))
            throw new Exception();

        world.initAllRandomVars();
        setStartTime(new Date());
        setStartWorldState(world);

        long startTimeMillis = System.currentTimeMillis();
        simulationState = SimulationState.RUNNING;

        while (isSimulationFinished(startTimeMillis).isEmpty() && simulationState != SimulationState.ERROR) {
            ticks++;
            handleSingleTick();
            ActionsPerformer.updateStableTimeToAllProps(world);
        }

        if (simulationState != SimulationState.ERROR)
            EngineLoggers.SIMULATION_LOGGER.info(String.format("Simulation [%s] ended due to [%s] condition reached", uuid, isSimulationFinished(startTimeMillis)));

        else
            EngineLoggers.SIMULATION_LOGGER.info(String.format("Simulation [%s] ended due to runtime error", uuid));

        setEndTime(new Date());
        setFinishWorldState(world);
    }
    public World getWorld() { return world; }
    public SimulationState getSimulationState() { return simulationState; }
    @Override
    public String toString() {
        return "--------------------------------------\n" +
                "----------Simulation details-----------\n" +
                "--------------------------------------\n" +
                world.toString();
    }
}