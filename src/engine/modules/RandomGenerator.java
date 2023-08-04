package engine.modules;

import java.util.Random;

public class RandomGenerator {
    public static int randomizeRandomNumber(int max) {
        return new Random().nextInt(max);
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

    public static String randomizeRandomString(int length) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
