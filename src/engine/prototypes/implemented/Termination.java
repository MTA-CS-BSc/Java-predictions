package engine.prototypes.implemented;

import engine.prototypes.jaxb.PRDBySecond;
import engine.prototypes.jaxb.PRDByTicks;
import engine.prototypes.jaxb.PRDTermination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Termination implements Serializable {
    protected List<Object> stopConditions;
    protected boolean isStopByUser;
    public Termination (PRDTermination termination) {
        stopConditions = new ArrayList<>();

        if (!Objects.isNull(termination)) {
            termination.getPRDBySecondOrPRDByTicks().forEach(stopCondition -> {
                if (stopCondition.getClass() == PRDByTicks.class)
                    stopConditions.add(new ByTicks((PRDByTicks) stopCondition));

                else if (stopCondition.getClass() == PRDBySecond.class)
                    stopConditions.add(new BySecond((PRDBySecond) stopCondition));
            });

            isStopByUser = !Objects.isNull(termination.getPRDByUser());
        }
    }
    public List<Object> getStopConditions() {
        return stopConditions;
    }
    public boolean isStopByUser() { return isStopByUser; }
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

        if (isStopByUser) {
            sb.append("#####Stop Condition######\n");
            sb.append("Termination by user\n");
        }

        return sb.toString();
    }
}
