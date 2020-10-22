package de.timmi6790.mpmod.utilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtilities {
    public long getUnixTime() {
        return (long) (System.currentTimeMillis() / 1000F);
    }

    public long getCurrentTimeMinusTime(final int time) {
        return getUnixTime() - time;
    }
}
