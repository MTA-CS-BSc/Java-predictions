package engine.simulation;

import engine.prototypes.jaxb.PRDBySecond;
import engine.prototypes.jaxb.PRDByTicks;
import engine.prototypes.jaxb.PRDTermination;
import engine.prototypes.parsed.World;

public class SingleSimulation {
    World world;
    long ticks = 0;

    public SingleSimulation(World _world) {
        world = _world;
    }

    public boolean isDone(long startTimeMillis) {
        PRDTermination termination = world.getTermination();

        for (Object stopCondition : termination.getPRDByTicksOrPRDBySecond()) {
            if (stopCondition.getClass() == PRDBySecond.class
                    && startTimeMillis >= ((PRDBySecond)stopCondition).getCount())
                return true;

            else if (stopCondition.getClass() == PRDByTicks.class
                    && ticks >= ((PRDByTicks)stopCondition).getCount())
                return true;
        }

        return false;
    }

    public void run() {
        long startTimeMillis = System.currentTimeMillis();

        while (!isDone(startTimeMillis)) {

        }
    }
}
