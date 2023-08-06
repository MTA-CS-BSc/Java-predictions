package engine.simulation;

import engine.logs.Loggers;
import engine.logs.SingleSimulationLog;
import engine.modules.RandomGenerator;
import engine.consts.TerminationReasons;
import engine.prototypes.implemented.World;
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
        PRDTermination termination = world.getTermination();

        for (Object stopCondition : termination.getPRDByTicksOrPRDBySecond()) {
            if (stopCondition.getClass() == PRDBySecond.class
                    && startTimeMillis >= ((PRDBySecond)stopCondition).getCount())
                return TerminationReasons.BY_SECOND;

            else if (stopCondition.getClass() == PRDByTicks.class
                    && ticks >= ((PRDByTicks)stopCondition).getCount())
                return TerminationReasons.BY_TICKS;
        }

        return null;
    }
    public boolean isPossibleToPerformRule(HashMap<String, PRDRule> allRules, String ruleName) {
        float randomProbability = RandomGenerator.randomizeProbability();
        PRDRule rule = allRules.get(ruleName);

        return rule.getPRDActivation().getProbability() >= randomProbability
                && ticks % rule.getPRDActivation().getTicks() == 0;
    }
    public HashMap<String, PRDRule> getRulesToApply() {
        HashMap<String, PRDRule> allRules = world.getRules().getRulesMap();
        List<String> relevantKeys = allRules.keySet()
                                    .stream()
                                    .filter(element -> isPossibleToPerformRule(allRules, element))
                                    .collect(Collectors.toList());

        HashMap<String, PRDRule> returned = new HashMap<>();

        relevantKeys.forEach(key -> {
           returned.put(key, allRules.get(key));
        });

        return returned;
    }
    public void handleSingleTick() {
        HashMap<String, PRDRule> rulesToApply = getRulesToApply();

        rulesToApply.forEach((ruleName, rule) -> {
            List<PRDAction> actionsToPerform = rule.getPRDActions().getPRDAction();
            actionsToPerform.forEach(action -> performer.fireAction(world, action, null));
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