package engine.simulation;

import engine.logs.Loggers;
import engine.prototypes.jaxb.PRDBySecond;
import engine.prototypes.jaxb.PRDByTicks;
import engine.prototypes.jaxb.PRDTermination;
import engine.prototypes.parsed.World;

import java.util.Objects;
import java.util.UUID;

public class SingleSimulation {
    World world;
    long ticks = 0;
    UUID uuid;

    public SingleSimulation(World _world) {
        uuid = UUID.randomUUID();
        world = _world;
    }
    public String isDone(long startTimeMillis) {
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
    public void run() {
        long startTimeMillis = System.currentTimeMillis();

        while (Objects.isNull(isDone(startTimeMillis))) {
            // TODO: Implement run simulation
        }

        Loggers.SIMULATION_LOGGER.info(String.format("Simulation [%s] ended due to [%s] condition reached",
                                            uuid.toString(), isDone(startTimeMillis)));
    }
}
