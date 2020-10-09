package de.timmi6790.mcmod.utilities;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class MathUtilities {
    public double roundDouble(final double value, final int factor) {
        final double limit = 1.0 * Math.pow(10, factor);
        return Math.round(value * limit) / limit;
    }

    public double getRandomDouble(double min, final double max) {
        if (min > max) {
            min = max;
        }

        return min + (max - min) * ThreadLocalRandom.current().nextDouble();
    }
}
