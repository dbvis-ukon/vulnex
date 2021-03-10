package de.dbvis.sparta.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    public static int randomIntInRange(final int min, final int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min!");
        }
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static double randomDoubleInRange(final double min, final double max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min!");
        }
        return (max - min) * new Random().nextDouble() + min;
    }

    public static List<Boolean> createRandomBugPresenceList(final int size) {
        List<Boolean> result = new ArrayList<Boolean>(size);
        for (int i = 0; i < size; i++) {
            result.add(new Random().nextBoolean());
        }
        return result;
    }

}
