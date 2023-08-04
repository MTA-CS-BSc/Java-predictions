package engine.simulation;

import engine.logs.Loggers;
import engine.modules.RandomGenerator;
import engine.prototypes.jaxb.*;
import engine.prototypes.implemented.World;

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
    public void handleSingleTick() {
        HashMap<String, PRDRule> rulesToApply = getRelevantRules();

        //Todo apply rules
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
