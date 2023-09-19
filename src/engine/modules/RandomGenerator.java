package engine.modules;

import engine.prototypes.implemented.Coordinate;
import engine.prototypes.implemented.WorldGrid;
import helpers.Constants;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class RandomGenerator {
    public static Integer randomizeRandomNumber(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }

    public static boolean randomizeRandomBoolean() {
        return new Random().nextBoolean();
    }

    public static float randomizeProbability() {
        return new Random().nextFloat();
    }

    public static float randomizeFloat(float min, float max) {
        return (new Random().nextFloat() * (max - min)) + min;
    }

    public static String randomizeRandomString() {
        int randomLength = randomizeRandomNumber(1, Constants.MAX_RANDOM_STRING_LENGTH);
        StringBuilder sb = new StringBuilder(randomLength);

        for (int i = 0; i < randomLength; i++) {
            int randomIndex = new Random().nextInt(Constants.STRING_ALLOWED_CHARS.length());
            char randomSymbol = Constants.STRING_ALLOWED_CHARS.charAt(randomIndex);
            sb.append(randomSymbol);
        }

        return sb.toString();
    }

    public static Coordinate randomizeRandomCoordinate(WorldGrid grid) {
        Coordinate randomCoordinate = new Coordinate(RandomGenerator.randomizeRandomNumber(0, grid.getRows() - 1),
                RandomGenerator.randomizeRandomNumber(0, grid.getColumns() - 1));

        while (isCoordinateTaken(grid, randomCoordinate)) {
            randomCoordinate.setX(RandomGenerator.randomizeRandomNumber(0, grid.getRows() - 1));
            randomCoordinate.setY(RandomGenerator.randomizeRandomNumber(0, grid.getColumns() - 1));
        }

        return randomCoordinate;
    }

    public static boolean isCoordinateTaken(WorldGrid grid, Coordinate coordinate) {
        return grid.isTaken(coordinate);
    }

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