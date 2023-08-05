package engine.simulation;

import engine.logs.Loggers;
import engine.modules.ActionTypes;
import engine.modules.RandomGenerator;
import engine.modules.TerminationReasons;
import engine.prototypes.implemented.World;
import engine.prototypes.jaxb.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class SingleSimulation {
    World world;
    long ticks = 0;
    UUID uuid;
    ActionsPerformer performer;

    //TODO: Add simulation history
    public SingleSimulation(World _world) {
        uuid = UUID.randomUUID();
        performer = new ActionsPerformer();
        world = _world;
    }
    public void updateStableTimeToAllProps() {
        world.getEntities().getEntitiesMap().values().forEach(entity -> {
            entity.getProperties().getPropsMap().values().forEach(property -> {
                property.setStableTime(property.getStableTime() + 1);
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
    public void fireAction(PRDAction action) {
        String type = action.getType();

        if (type.equalsIgnoreCase(ActionTypes.INCREASE)
                || type.equalsIgnoreCase(ActionTypes.DECREASE))
           performer.handleIncrementDecrementAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.CALCULATION))
            performer.handleCalculationAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.SET))
            performer.handleSetAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.KILL))
            performer.handleKillAction(world, action);

        else if (type.equalsIgnoreCase(ActionTypes.CONDITION))
            performer.handleConditionAction(world, action);
    }
    public void handleSingleTick() {
        HashMap<String, PRDRule> rulesToApply = getRulesToApply();

        rulesToApply.forEach((ruleName, rule) -> {
            List<PRDAction> actionsToPerform = rule.getPRDActions().getPRDAction();
            actionsToPerform.forEach(this::fireAction);
        });
    }
    public String run() {
        if (Objects.isNull(world)) {
            Loggers.SIMULATION_LOGGER.info("No XML loaded");
            return "";
        }

        world.initAllRandomVars();
        long startTimeMillis = System.currentTimeMillis();

        while (Objects.isNull(isSimulationFinished(startTimeMillis))) {
            handleSingleTick();
            ticks += 1;
            updateStableTimeToAllProps();
        }

        Loggers.SIMULATION_LOGGER.info(String.format("Simulation [%s] ended due to [%s] condition reached",
                                            uuid.toString(), isSimulationFinished(startTimeMillis)));

        return uuid.toString();
    }
}
