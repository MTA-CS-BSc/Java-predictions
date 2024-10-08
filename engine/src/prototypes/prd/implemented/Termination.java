package prototypes.prd.implemented;

import other.TerminationDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Termination implements Serializable {
    protected List<Object> stopConditions;
    protected boolean isStopByUser;

    public Termination() {
        stopConditions = new ArrayList<>();
        isStopByUser = false;
    }

    public Termination(Termination other) {
        stopConditions = new ArrayList<>();

        other.getStopConditions().forEach(this::addStopCondition);

        isStopByUser = other.isStopByUser();
    }

    public Termination(TerminationDTO other) {
        stopConditions = new ArrayList<>();

        other.getStopConditions().forEach(element -> {
            if (element.getByWho().equals("ticks"))
                stopConditions.add(new ByTicks(element.getCount()));

            else
                stopConditions.add(new BySecond(element.getCount()));
        });

        isStopByUser = other.isByUser();
    }

    public void addStopCondition(Object stopCondition) {
        if (stopCondition.getClass() == ByTicks.class)
            stopConditions.add(new ByTicks((ByTicks) stopCondition));

        else if (stopCondition.getClass() == BySecond.class)
            stopConditions.add(new BySecond((BySecond) stopCondition));

        isStopByUser = false;
    }

    public void setStopByUser(boolean value) {
        if (!stopConditions.isEmpty() && value)
            stopConditions.clear();

        isStopByUser = value;
    }

    public List<Object> getStopConditions() {
        return stopConditions;
    }

    public boolean isStopByUser() {
        return isStopByUser;
    }
}
