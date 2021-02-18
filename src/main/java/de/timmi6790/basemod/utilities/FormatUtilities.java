package de.timmi6790.basemod.utilities;

import lombok.experimental.UtilityClass;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

@UtilityClass
public class FormatUtilities {
    public static String toHumanReadableSeconds(final long seconds) {
        return String.format(
                "%02d:%02d:%02d",
                seconds / 3600,
                (seconds % 3600) / 60,
                (seconds % 60)
        );
    }

    public static String toHumanReadableByteCountBin(final long bytes) {
        final long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        final CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }
}
