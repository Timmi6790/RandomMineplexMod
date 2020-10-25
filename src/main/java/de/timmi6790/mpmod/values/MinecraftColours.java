package de.timmi6790.mpmod.values;

import de.timmi6790.mpmod.utilities.StringUtilities;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum MinecraftColours {
    DARK_RED("&4"),
    RED("&c"),
    GOLD("&6"),
    YELLOW("&e"),
    DARK_GREEN("&2"),
    GREEN("&a"),
    AQUA("&b"),
    DARK_AQUA("&3"),
    DARK_BLUE("&1"),
    BLUE("&9"),
    LIGHT_PURPLE("&d"),
    DARK_PURPLE("&5"),
    WHITE("&f"),
    GRAY("&8"),
    DARK_GRAY("&8"),
    BLACK("&0");

    private final String colourCode;

    public static List<String> getAllColourNames() {
        final List<String> colourNames = new ArrayList<>();
        for (final MinecraftColours colour : MinecraftColours.values()) {
            colourNames.add(colour.getFormattedName());
        }

        return colourNames;
    }

    public static Optional<MinecraftColours> getMinecraftColour(final String name) {
        for (final MinecraftColours colour : MinecraftColours.values()) {
            if (colour.getFormattedName().equalsIgnoreCase(name)) {
                return Optional.of(colour);
            }
        }

        return Optional.empty();
    }

    public String getFormattedName() {
        final String[] nameParts = this.name().split("_");
        final StringBuilder name = new StringBuilder();
        for (final String part : nameParts) {
            name.append(StringUtilities.capitalize(part));
        }

        return name.toString();
    }
}
