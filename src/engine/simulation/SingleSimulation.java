package engine.simulation;

import engine.logs.Loggers;
import engine.modules.ActionTypes;
import engine.modules.RandomGenerator;
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

    public SingleSimulation(World _world) {
        uuid = UUID.randomUUID();
        world = _world;
    }
    public String isSimulationFinished(long startTimeMillis) {
        PRDTermination termination = world.getTermination();

        for (Object stopCondition : termination.getPRDByTicksOrPRDBySecond()) {
            if (stopCondition.getClass() == PRDBySecond.class
                    && startTimeMillis >= ((PRDBySecond)stopCondition).getCount())
                return "BySecond";

            else if (stopCondition.getClass() == PRDByTicks.class
                    && ticks >= ((PRDByTicks)stopCondition).getCount())
                return "ByTicks";
        }

        return null;
    }
    public boolean isPossibleToPerformRule(HashMap<String, PRDRule> allRules, String ruleName) {
        float randomProbability = RandomGenerator.randomizeProbability();
        PRDRule rule = allRules.get(ruleName);

        return rule.getPRDActivation().getProbability() >= randomProbability
                && rule.getPRDActivation().getTicks() % ticks == 0;
    }
    public HashMap<String, PRDRule> getRelevantRules() {
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
    public void handleKillAction(PRDAction action) {
        Loggers.SIMULATION_LOGGER.info(String.format("Killing entity [%s]", action.getEntity()));
        world.getEntities().getEntitiesMap().remove(action.getEntity());
    }
    public void fireAction(PRDAction action) {
        String type = action.getType();

        if (type.equalsIgnoreCase(ActionTypes.INCREASE)
                || type.equalsIgnoreCase(ActionTypes.DECREASE))
            Loggers.ENGINE_LOGGER.warning("Not implemented");
            //handleIncrementDecrementAction(action);

        else if (type.equalsIgnoreCase(ActionTypes.CALCULATION))
            Loggers.ENGINE_LOGGER.warning("Not implemented");
            //handleCalculationAction(action);

        else if (type.equalsIgnoreCase(ActionTypes.SET))
            Loggers.ENGINE_LOGGER.warning("Not implemented");
            //handleSetAction(action);

        else if (type.equalsIgnoreCase(ActionTypes.KILL))
            handleKillAction(action);

        else if (type.equalsIgnoreCase(ActionTypes.CONDITION))
            Loggers.ENGINE_LOGGER.warning("Not implemented");
            //handleConditionAction(action);
    }
    public void handleSingleTick() {
        HashMap<String, PRDRule> rulesToApply = getRelevantRules();

        rulesToApply.forEach((ruleName, rule) -> {
            List<PRDAction> actionsToPerform = rule.getPRDActions().getPRDAction();
            actionsToPerform.forEach(this::fireAction);
        });
    }

    public void run() {
        long startTimeMillis = System.currentTimeMillis();

        while (Objects.isNull(isSimulationFinished(startTimeMillis))) {
            handleSingleTick();
            ticks += 1;
        }

        Loggers.SIMULATION_LOGGER.info(String.format("Simulation [%s] ended due to [%s] condition reached",
                                            uuid.toString(), isSimulationFinished(startTimeMillis)));
    }
}
