package de.timmi6790.mpmod.utilities;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtilities {
    public String repeatString(final String string, final long count) {
        final StringBuilder repeat = new StringBuilder();
        for (int i = 0; i < count; i++) {
            repeat.append(string);
        }

        return repeat.toString();
    }

    public String capitalize(final String string) {
        if (string.isEmpty()) {
            return string;
        }

        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public String removeColourCodes(final String message) {
        return message.replaceAll("(§.)", "");
    }
}
