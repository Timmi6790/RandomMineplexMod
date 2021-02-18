package de.timmi6790.basemod.utilities;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.StringTokenizer;

@UtilityClass
public class VersionUtilities {
    private String getTokenOrDefault(final StringTokenizer tokenizer, final String defaultValue) {
        if (tokenizer.hasMoreTokens()) {
            return tokenizer.nextToken();
        }

        return defaultValue;
    }

    public boolean hasNewVersion(@NonNull final String currentVersion, @NonNull final String newVersion) {
        final StringTokenizer currentVersionToken = new StringTokenizer(currentVersion, ".");
        final StringTokenizer newVersionToken = new StringTokenizer(newVersion, ".");

        while (currentVersionToken.hasMoreTokens() || newVersionToken.hasMoreElements()) {
            final String currentPart = getTokenOrDefault(currentVersionToken, "0");
            final String newPart = getTokenOrDefault(newVersionToken, "0");

            final int compareValue = newPart.compareTo(currentPart);
            if (compareValue > 0) {
                return true;
            } else if (compareValue < 0) {
                return false;
            }
        }

        // Equal versions
        return false;
    }
}
