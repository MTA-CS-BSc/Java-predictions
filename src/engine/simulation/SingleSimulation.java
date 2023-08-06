package engine.simulation;

import engine.logs.Loggers;
import engine.logs.SingleSimulationLog;
import engine.modules.RandomGenerator;
import engine.consts.TerminationReasons;
import engine.prototypes.implemented.*;
import engine.prototypes.jaxb.*;

import java.util.*;
import java.util.stream.Collectors;

public class SingleSimulation {
    World world;
    long ticks = 0;
    UUID uuid;
    ActionsPerformer performer;
    SingleSimulationLog log;

    public SingleSimulation(World _world) {
        uuid = UUID.randomUUID();
        performer = new ActionsPerformer();
        world = _world;
        log = new SingleSimulationLog(uuid, world);
    }
    public void updateStableTimeToAllProps() {
        world.getEntities().getEntitiesMap().values().forEach(entity -> {
            entity.getSingleEntities().forEach(singleEntity -> {
                singleEntity.getProperties().getPropsMap().forEach((key, prop) -> {
                    prop.setStableTime(prop.getStableTime() + 1);
                });
            });
        });
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
    public boolean isPossibleToPerformRule(HashMap<String, Rule> allRules, String ruleName) {
        float randomProbability = RandomGenerator.randomizeProbability();
        Rule rule = allRules.get(ruleName);

        return rule.getActivation().getProbability() >= randomProbability
                && ticks % rule.getActivation().getTicks() == 0;
    }
    public HashMap<String, Rule> getRulesToApply() {
        HashMap<String, Rule> allRules = world.getRules().getRulesMap();

        List<String> relevantKeys = allRules.keySet()
                                    .stream()
                                    .filter(element -> isPossibleToPerformRule(allRules, element))
                                    .collect(Collectors.toList());

        HashMap<String, Rule> returned = new HashMap<>();

        relevantKeys.forEach(key -> {
           returned.put(key, allRules.get(key));
        });

        return returned;
    }
    public void handleSingleTick() {
        HashMap<String, Rule> rulesToApply = getRulesToApply();

        rulesToApply.forEach((ruleName, rule) -> {
            Actions actionsToPerform = rule.getActions();
            actionsToPerform.getActions().forEach(action -> performer.fireAction(world, action, null));
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
            handleSingleTick();
            ticks += 1;
            updateStableTimeToAllProps();
        }

        Loggers.SIMULATION_LOGGER.info(String.format("Simulation [%s] ended due to [%s] condition reached",
                                            uuid.toString(), isSimulationFinished(startTimeMillis)));

        log.setFinished(new Date());
        log.setFinishWorldState(world);
        return uuid.toString();
    }
}