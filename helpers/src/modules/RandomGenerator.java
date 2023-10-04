package modules;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public abstract class RandomGenerator {
    public static List<Color> generateDistinctColors(int entitiesAmount) {
        List<Color> colors = new ArrayList<>();

        for (int i = 0; i < entitiesAmount; i++) {
            double hue = (i * 360.0) / entitiesAmount; // Distribute hues evenly
            double saturation = 1.0;     // Full saturation
            double brightness = 1.0;     // Full brightness

            Color color = Color.hsb(hue, saturation, brightness);
            colors.add(color);
        }

        return colors;
    }
}
