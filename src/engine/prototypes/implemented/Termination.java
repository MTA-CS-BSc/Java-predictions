package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDBySecond;
import engine.prototypes.jaxb.PRDByTicks;
import engine.prototypes.jaxb.PRDTermination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Termination implements Serializable {
    List<Object> stopConditions;
    public Termination (PRDTermination _termination) {
        stopConditions = new ArrayList<>();

        if (!Objects.isNull(_termination)) {
            _termination.getPRDBySecondOrPRDByTicks().forEach(_stopCondition -> {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("###########Termination###########\n");

        getStopConditions().forEach(stopCondition -> {
            sb.append("#####Stop Condition######\n");

            if (stopCondition.getClass() == ByTicks.class)
                sb.append("Stop after [").append(((ByTicks)stopCondition).getCount()).append("] ticks\n");

            else if (stopCondition.getClass() == BySecond.class)
                sb.append("Stop after [").append(((BySecond)stopCondition).getCount()).append("] seconds\n");
        });

        return sb.toString();
    }
}
