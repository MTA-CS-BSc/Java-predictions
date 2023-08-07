package engine.simulation;

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
        HashMap<String, Rule> rulesToApply = Utils.getRulesToApply(world, ticks);

        rulesToApply.forEach((ruleName, rule) -> {
            Actions actionsToPerform = rule.getActions();
            actionsToPerform.getActions().forEach(action -> ActionsPerformer.fireAction(world, action, null));
        });
    }
    public String run() {
        if (Objects.isNull(world)) {
            Loggers.SIMULATION_LOGGER.info("No XML loaded");
            return "";
        }

        world.initAllRandomVars();
        log.setStart(new Date());

        long startTimeMillis = System.currentTimeMillis();

        while (Objects.isNull(isSimulationFinished(startTimeMillis))) {
            ticks += 1;
            handleSingleTick();
            ActionsPerformer.updateStableTimeToAllProps(world);
        }

        Loggers.SIMULATION_LOGGER.info(String.format("Simulation [%s] ended due to [%s] condition reached",
                                            uuid.toString(), isSimulationFinished(startTimeMillis)));

        log.setFinished(new Date());
        log.setFinishWorldState(world);
        return uuid.toString();
    }
}