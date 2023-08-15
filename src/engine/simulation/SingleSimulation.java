package engine.simulation;

import engine.consts.SimulationState;
import engine.logs.EngineLoggers;
import engine.consts.TerminationReasons;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import engine.simulation.performers.ActionsPerformer;

import java.io.Serializable;
import java.util.*;

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
        Map<String, Rule> rulesToApply = Utils.getRulesToApply(world, ticks);

        rulesToApply.forEach((ruleName, rule) -> {
            Actions actionsToPerform = rule.getActions();
            actionsToPerform.getActions().forEach(action -> ActionsPerformer.fireAction(world, action, null));
        });
    }
    public void run() throws Exception {
        if (Objects.isNull(world))
            throw new Exception();

        world.initAllRandomVars();
        setStartTime(new Date());
        setStartWorldState(world);

        long startTimeMillis = System.currentTimeMillis();
        simulationState = SimulationState.RUNNING;

        while (isSimulationFinished(startTimeMillis).isEmpty()) {
            ticks++;
            handleSingleTick();
            ActionsPerformer.updateStableTimeToAllProps(world);
        }

        EngineLoggers.SIMULATION_LOGGER.info(String.format("Simulation [%s] ended due to [%s] condition reached",
                                            uuid, isSimulationFinished(startTimeMillis)));

        setEndTime(new Date());
        setFinishWorldState(world);
    }
    public World getWorld() { return world; }
    @Override
    public String toString() {
        return "--------------------------------------\n" +
                "----------Simulation details-----------\n" +
                "--------------------------------------\n" +
                world.toString();
    }
}