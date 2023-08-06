package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDBySecond;
import engine.prototypes.jaxb.PRDByTicks;
import engine.prototypes.jaxb.PRDTermination;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Termination {
    List<Object> stopConditions;
    public Termination (PRDTermination _termination) {
        stopConditions = new ArrayList<>();

        if (!Objects.isNull(_termination)) {
            _termination.getPRDByTicksOrPRDBySecond().forEach(_stopCondition -> {
                if (_stopCondition.getClass() == PRDByTicks.class)
                    stopConditions.add(new ByTicks((PRDByTicks) _stopCondition));

                else if (_stopCondition.getClass() == PRDBySecond.class)
                    stopConditions.add(new BySecond((PRDBySecond) _stopCondition));
            });
        }
    }
    public List<Object> getStopConditions() {
        return stopConditions;
    }
}
