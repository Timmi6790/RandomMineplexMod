package de.timmi6790.mpmod.modules.community.tabsupport;

import de.timmi6790.mpmod.tabsupport.AbstractTabSupport;
import de.timmi6790.mpmod.tabsupport.TabSupportData;
import de.timmi6790.mpmod.utilities.DataUtilities;
import de.timmi6790.mpmod.values.BukkitValues;
import lombok.Getter;

import java.util.*;

public class ImmortalGiveTabSupport extends AbstractTabSupport {
    private static final String[] AMOUNT_EXAMPLE = new String[]{"1", "10", "32", "64", "128", "256", "512", "1000"};
    private static final String[] ENCHANTMENT_EXAMPLE = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    public ImmortalGiveTabSupport() {
        super("/ig", "/e");

        this.setCancelServerRequest(true);
    }

    @Override
    public void onTabSupport(final TabSupportData tabSupportData) {
        if (!tabSupportData.getMessage().toLowerCase().startsWith("/ig")
                && !tabSupportData.getMessage().toLowerCase().startsWith("/e give")) {
            return;
        }

        final String[] args = tabSupportData.getMessageArgs();
        if (args.length == 2) {
            this.handleParametersOptions(tabSupportData);
            return;
        }

        final String lastArg = args[args.length - 1];
        final String previousArg = args[args.length - 2];

        // Only parameter that can stand alone
        if (previousArg.equalsIgnoreCase("-all")) {
            this.handleParametersOptions(tabSupportData);
            return;
        }

        final Optional<Parameters> parametersOpt = Parameters.getFromName(previousArg);
        if (!parametersOpt.isPresent()) {
            // We can assume that if the prev arg is not a valid parameter the lastArg is a parameter, or the input is incorrect
            this.handleParametersOptions(tabSupportData);
            return;
        }

        switch (parametersOpt.get()) {
            case PLAYER:
                this.handlePlayerOptions(lastArg, tabSupportData);
                return;
            case ITEM:
                this.handleItemOptions(lastArg, tabSupportData);
                return;
            case AMOUNT:
                this.handleAmountOptions(lastArg, tabSupportData);
                return;
            case ENCHANTS:
                this.handleEnchantOptions(lastArg, tabSupportData);
                return;
            default:
        }
    }

    private void handleParametersOptions(final TabSupportData tabSupportData) {
        final String[] args = Arrays.copyOfRange(tabSupportData.getMessageArgs(), 1, tabSupportData.getMessageArgs().length);
        final EnumSet<Parameters> availableParameters = EnumSet.allOf(Parameters.class);
        for (final String arg : args) {
            final Iterator<Parameters> iterator = availableParameters.iterator();
            while (iterator.hasNext()) {
                for (final String name : iterator.next().getNames()) {
                    if (name.equalsIgnoreCase(arg)) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }

        final List<String> availableNames = new ArrayList<>();
        for (final Parameters parameters : availableParameters) {
            availableNames.addAll(Arrays.asList(parameters.getNames()));
        }

        tabSupportData.setTabReturn(DataUtilities.getStartWithIgnoreCase(availableNames, args[args.length - 1]));
    }

    private void handlePlayerOptions(final String lastArg, final TabSupportData tabSupportData) {
        final String[] playerNames = lastArg.split(",");
        final String lastPlayerName = playerNames[playerNames.length - 1];

        final Set<String> playersInLobby = this.getAllPlayersInLobby();
        for (final String player : playerNames) {
            DataUtilities.removeIgnoreCase(playersInLobby, player);
        }

        // Separator | End of name
        if (lastArg.endsWith(",") || DataUtilities.containsIgnoreCase(this.getAllPlayersInLobby(), lastPlayerName)) {
            final String prefix = String.join(",", playerNames) + ",";
            tabSupportData.setTabReturn(DataUtilities.combine(prefix, playersInLobby));
            return;
        }

        // Unfinished name
        final String[] usedNames = Arrays.copyOfRange(playerNames, 0, playerNames.length - 1);
        final String prefix = String.join(",", usedNames) + (usedNames.length > 0 ? "," : "");
        tabSupportData.setTabReturn(DataUtilities.combine(prefix, DataUtilities.getStartWithIgnoreCase(playersInLobby, lastPlayerName)));
    }

    private void handleItemOptions(final String lastArg, final TabSupportData tabSupportData) {
        tabSupportData.setTabReturn(DataUtilities.getStartWithIgnoreCase(DataUtilities.collectionToLowerCase(Arrays.asList(BukkitValues.getMaterials())), lastArg));
    }

    private void handleAmountOptions(final String lastArg, final TabSupportData tabSupportData) {
        tabSupportData.setTabReturn(DataUtilities.getStartWithIgnoreCase(AMOUNT_EXAMPLE, lastArg));
    }

    private void handleEnchantOptions(final String lastArg, final TabSupportData tabSupportData) {
        final String[] enchantParts = lastArg.split(",");
        final String[] lastEnchantPart = enchantParts[enchantParts.length - 1].split(":");

        final Set<String> enchantments = new HashSet<>(DataUtilities.collectionToLowerCase(Arrays.asList(BukkitValues.getEnchantments())));
        for (final String part : enchantParts) {
            final String[] split = part.split(":");
            if (split.length == 2) {
                DataUtilities.removeIgnoreCase(enchantments, split[0]);
            }
        }

        // Separator | End of part
        if (lastArg.endsWith(",") || (lastEnchantPart.length == 2 && DataUtilities.isInt(lastEnchantPart[1]))) {
            final String prefix = String.join(",", enchantParts) + ",";
            tabSupportData.setTabReturn(DataUtilities.combine(prefix, enchantments));
            return;
        }

        final String[] usedEnchants = Arrays.copyOfRange(enchantParts, 0, enchantParts.length - 1);
        final String prefix = String.join(",", usedEnchants) + (usedEnchants.length > 0 ? "," : "");
        if (lastEnchantPart.length == 1 && DataUtilities.containsIgnoreCase(Arrays.asList(BukkitValues.getEnchantments()), lastEnchantPart[0])) {
            // Add level
            tabSupportData.setTabReturn(DataUtilities.combine(prefix + lastEnchantPart[0] + ":", Arrays.asList(ENCHANTMENT_EXAMPLE)));
        } else {
            // Finish enchantment name
            tabSupportData.setTabReturn(DataUtilities.combine(prefix, DataUtilities.getStartWithIgnoreCase(enchantments, lastEnchantPart[0])));
        }
    }

    @Getter
    private enum Parameters {
        PLAYER("-player", "-p", "-all"),
        ITEM("-item", "-i"),
        AMOUNT("-amount", "-a"),
        ENCHANTS("-enchants", "-e");

        private final String[] names;

        Parameters(final String... names) {
            this.names = names;
        }

        public static Optional<Parameters> getFromName(final String name) {
            for (final Parameters parameters : Parameters.values()) {
                for (final String parameterName : parameters.getNames()) {
                    if (parameterName.equalsIgnoreCase(name)) {
                        return Optional.of(parameters);
                    }
                }
            }

            return Optional.empty();
        }
    }
}
