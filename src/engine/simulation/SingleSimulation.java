package engine.simulation;

import engine.exceptions.NoXMLException;
import engine.logs.Loggers;
import engine.consts.TerminationReasons;
import engine.modules.Utils;
import engine.prototypes.implemented.*;
import engine.simulation.performers.ActionsPerformer;

import java.util.*;

public class SingleSimulation {
    World world;
    long ticks = 0;
    UUID uuid;

    SingleSimulationLog log;

    public SingleSimulation(World _world) {
        uuid = UUID.randomUUID();
        world = _world;
        log = new SingleSimulationLog(uuid, world);
    }
    public SingleSimulationLog getLog() {
        return log;
    }
    public String isSimulationFinished(long startTimeMillis) {
        Termination termination = world.getTermination();

        for (Object stopCondition : termination.getStopConditions()) {
            if (stopCondition.getClass() == BySecond.class
                    && System.currentTimeMillis() - startTimeMillis >= (long)((BySecond)stopCondition).getCount() * 1000)
                return TerminationReasons.BY_SECOND;

            else if (stopCondition.getClass() == ByTicks.class
                    && ticks >= ((ByTicks)stopCondition).getCount())
                return TerminationReasons.BY_TICKS;
        }

        return null;
    }
    public void handleSingleTick() {
        Map<String, Rule> rulesToApply = Utils.getRulesToApply(world, ticks);

        rulesToApply.forEach((ruleName, rule) -> {
            Actions actionsToPerform = rule.getActions();
            actionsToPerform.getActions().forEach(action -> ActionsPerformer.fireAction(world, action, null));
        });
    }
    public String run() throws NoXMLException {
        if (Objects.isNull(world))
            throw new NoXMLException();

        world.initAllRandomVars();
        log.setStartTime(new Date());

        long startTimeMillis = System.currentTimeMillis();

        while (Objects.isNull(isSimulationFinished(startTimeMillis))) {
            ticks++;
            handleSingleTick();
            ActionsPerformer.updateStableTimeToAllProps(world);
        }

        Loggers.SIMULATION_LOGGER.info(String.format("Simulation [%s] ended due to [%s] condition reached",
                                            uuid.toString(), isSimulationFinished(startTimeMillis)));

        log.setEndTime(new Date());
        log.setFinishWorldState(world);
        return uuid.toString();
    }
}