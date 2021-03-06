package de.timmi6790.basemod.utilities;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Enum utilities.
 */
@UtilityClass
public class EnumUtilities {
    /**
     * Converts all enum value into a more readable name.
     *
     * @param enumValue the enum value
     * @return the pretty names
     */
    public List<String> getPrettyNames(@NonNull final Enum[] enumValue) {
        return Arrays.stream(enumValue)
                .map(EnumUtilities::getPrettyName)
                .collect(Collectors.toList());
    }

    /**
     * Converts a enum value into a neat formatted text. It will remove all _ characters and will also capitalize all
     * values after the first part. TEST_Value -> TestValue
     *
     * @param enumValue the enum value
     * @return the pretty name
     */
    public String getPrettyName(@NonNull final Enum enumValue) {
        // Remove _ and capitalize after the first part
        final String[] nameParts = enumValue.name().split("_");
        final StringBuilder prettyName = new StringBuilder();
        for (final String namePart : nameParts) {
            prettyName.append(StringUtilities.capitalize(namePart.toLowerCase()));
        }

        return prettyName.toString();
    }

    /**
     * Searches for the search string against the given enum values. All enum values are checked with their pretty name
     * {@link #getPrettyName(Enum)}}.
     *
     * @param <T>       the enum type
     * @param search    the search string
     * @param enumValue the enum value
     * @return the found enum value
     */
    public <T extends Enum> Optional<T> getIgnoreCase(@NonNull final String search, @NonNull final T[] enumValue) {
        return Arrays.stream(enumValue)
                .filter(value -> getPrettyName(value).equalsIgnoreCase(search))
                .findAny();
    }
}
