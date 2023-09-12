package engine.modules;

import helpers.Constants;

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
        return min + new Random().nextFloat() * (max - min);
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
}