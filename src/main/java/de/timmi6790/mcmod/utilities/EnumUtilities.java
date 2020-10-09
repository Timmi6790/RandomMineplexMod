package de.timmi6790.mcmod.utilities;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class EnumUtilities {
    public List<String> getPrettyNames(final Enum[] enumValue) {
        return Arrays.stream(enumValue)
                .map(EnumUtilities::getPrettyName)
                .collect(Collectors.toList());
    }

    public String getPrettyName(final Enum enumValue) {
        return getPrettyName(enumValue.name());
    }

    public String getPrettyName(final String enumName) {
        if (enumName.isEmpty()) {
            return enumName;
        }

        // Remove _ and capitalize after the first part
        final String[] nameParts = enumName.split("_");
        final StringBuilder prettyName = new StringBuilder(nameParts[0].toLowerCase());
        for (int index = 1; nameParts.length > index; index++) {
            prettyName.append(StringUtilities.capitalize(nameParts[index].toLowerCase()));
        }

        return prettyName.toString();
    }

    public String getPrettyNameFirstUpper(final String enumName) {
        if (enumName.isEmpty()) {
            return enumName;
        }

        // Remove _ and capitalize after the first part
        final String[] nameParts = enumName.split("_");
        final StringBuilder prettyName = new StringBuilder();
        for (final String namePart : nameParts) {
            prettyName.append(StringUtilities.capitalize(namePart.toLowerCase()));
        }

        return prettyName.toString();
    }

    public <T extends Enum> Optional<T> getIgnoreCase(final String required, final T[] enumValue) {
        return Arrays.stream(enumValue)
                .filter(value -> getPrettyName(value).equalsIgnoreCase(required))
                .findAny();
    }
}
