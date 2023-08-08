package engine.modules;

import engine.consts.Restrictions;

import java.util.Random;

public class RandomGenerator {
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
        String symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!?,-.()0123456789 ";
        int randomLength = randomizeRandomNumber(1, Restrictions.MAX_RANDOM_STRING_LENGTH);
        StringBuilder sb = new StringBuilder(randomLength);

        for (int i = 0; i < randomLength; i++) {
            int randomIndex = new Random().nextInt(symbols.length());
            char randomSymbol = symbols.charAt(randomIndex);
            sb.append(randomSymbol);
        }

        return sb.toString();
    }
}