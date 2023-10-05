package prototypes.prd.implemented;

import prototypes.prd.generated.PRDActivation;

import java.io.Serializable;
import java.util.Objects;

public class Activation implements Serializable {
    protected int ticks;
    protected double probability;

    public Activation(PRDActivation activation) {
        if (!Objects.isNull(activation)) {
            ticks = !Objects.isNull(activation.getTicks()) ? activation.getTicks() : 1;
            probability = !Objects.isNull(activation.getProbability()) ? activation.getProbability() : 1;
        } else {
            ticks = 1;
            probability = 1.0;
        }
    }

    public Activation(Activation other) {
        this.ticks = other.getTicks();
        this.probability = other.getProbability();
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public double getProbability() {
        return probability;
    }
}
