package de.timmi6790.mpmod.modules.community.tabsupport;

import de.timmi6790.commons.utilities.EnumUtilities;
import de.timmi6790.mpmod.McMod;
import de.timmi6790.mpmod.tabsupport.AbstractTabSupport;
import de.timmi6790.mpmod.tabsupport.TabSupportData;
import de.timmi6790.mpmod.utilities.DataUtilities;
import de.timmi6790.mpmod.values.BukkitValues;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TabSupportEventCommand extends AbstractTabSupport {
    private static final String[] SCOREBOARD_LINES = IntStream.range(1, 16).mapToObj(Integer::toString).collect(Collectors.toList()).toArray(new String[0]);
    private static final String[] SCOREBOARD_CHAT_FORMATION_OPTIONS = {"&l", "&n", "&o", "&k", "&m", "&r", "&4", "&c", "&6", "&e",
            "&2", "&a", "&b", "&3", "&1", "&9", "&d", "&5", "&f", "&7"};

    private static final String[] SUGGESTED_MOB_NUMBERS = {"1", "32", "64", "128", "10000"};
    private static final String[] SUGGESTED_HEALTH = IntStream.range(1, 21).mapToObj(Integer::toString).collect(Collectors.toList()).toArray(new String[0]);
    private static final String[] SUGGESTED_FOOD = IntStream.range(1, 21).mapToObj(Integer::toString).collect(Collectors.toList()).toArray(new String[0]);
    private static final String[] SUGGESTED_EFFECT_LEVEL = {"1", "5", "10", "50", "100", "200", "255"};
    private static final String[] SUGGESTED_EFFECT_TIME = {"10", "100", "200", "1000", "1000000"};

    public TabSupportEventCommand() {
        super("/e");
        this.setCancelServerRequest(true);
    }

    @Override
    public void onTabSupport(final TabSupportData tabSupportData) {
        final String[] args = tabSupportData.getMessageArgs();
        if (args.length == 2) {
            tabSupportData.setTabReturn(DataUtilities.getStartWithIgnoreCase(EnumUtilities.getPrettyNames(FirstParameter.values()), args[1]));
            return;
        }

        final Optional<FirstParameter> parameterOpt = EnumUtilities.getIgnoreCase(args[1], FirstParameter.values());
        if (!parameterOpt.isPresent()) {
            return;
        }

        final String arg2 = args[2];
        switch (parameterOpt.get()) {
            case STACKER:
            case REVIVE:
            case ADMIN:
            case RADIUS:
            case DOUBLE_JUMP:
            case GM:
                this.handlePlayerNameOptions(tabSupportData, arg2);
                return;
            case MOB:
                this.handleMobOptions(tabSupportData, Arrays.copyOfRange(args, 2, args.length));
                return;
            case GADGET:
                this.handleGadgetOptions(tabSupportData, args);
                return;
            case TEMP_GADGET:
                this.handleTempGadgetOptions(tabSupportData, args);
                return;
            case SCOREBOARD:
                this.handleScoreboardOptions(tabSupportData, args);
                return;
            case DAMAGE:
                this.handleDamageOptions(tabSupportData, args);
                return;
            case TP:
                this.handleTpOptions(tabSupportData, args);
                return;
            case ITEM:
                this.handleItemOptions(tabSupportData, args);
                return;
            case CLEAR:
                this.handleClearOptions(tabSupportData, args);
                return;
            case EFFECT:
                this.handleEffectOptions(tabSupportData, args);
                return;
            case HEALTH:
                this.handleHealthOptions(tabSupportData, args);
                return;
            case HUNGER:
                this.handleHungerOptions(tabSupportData, args);
                return;
            case GAMEKIT:
                this.handleGamekitOptions(tabSupportData, args);
                return;
            case BLOCK_BREAK:
            case BLOCK_PLACE:
                this.handleBlockInteractionOptions(tabSupportData, args);
                return;
            case KIT:
                this.handleKitOptions(tabSupportData, args);
                return;
            case GIVE:
                // Handled in ImmortalGive
            case AREA:
            case DM:
            case ANNOUNCE:
            case SMASH:
            case BRIDGE:
            case SILENCE:
            case SETTINGS:
            case SUPPLY_DROP:
            case MOB_GRIEFING:
            case BLOCK_BREAK_CREATIVE:
            case BLOCK_PLACE_CREATIVE:
            default:
        }
    }

    private void handlePlayerNameOptions(final TabSupportData tabSupportData, final String arg) {
        tabSupportData.setTabReturn(this.getStartWithIgnoreCase(this.getAllPlayersInLobby(), arg));
    }

    private void handleMobOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 1) {
            final List<String> options = new ArrayList<>(Arrays.asList(BukkitValues.getMobs()));
            options.add(EnumUtilities.getPrettyName(MobParameter.KILL));

            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(options, args[0]));
            return;
        } else if (args.length == 2) {
            final Optional<MobParameter> mobParameterOpt = EnumUtilities.getIgnoreCase(args[0], MobParameter.values());
            if (mobParameterOpt.isPresent() && mobParameterOpt.get() == MobParameter.KILL) {
                final List<String> options = new ArrayList<>(Arrays.asList(BukkitValues.getMobs()));
                options.add("all");

                tabSupportData.setTabReturn(this.getStartWithIgnoreCase(options, args[1]));

            } else {
                tabSupportData.setTabReturn(DataUtilities.getStartWithIgnoreCase(SUGGESTED_MOB_NUMBERS, args[1]));
            }
            return;
        }

        final String lastArg = args[args.length - 1];
        if (lastArg.isEmpty() || lastArg.charAt(0) == ' ') {
            final EnumSet<MobSpawnParameter> usedParameters = EnumSet.allOf(MobSpawnParameter.class);
            for (int index = 2; args.length > index; index++) {
                final String arg = args[index].replace(" ", "");
                if (arg.length() == 0) {
                    continue;
                }

                EnumUtilities.getIgnoreCase(String.valueOf(arg.charAt(0)), MobSpawnParameter.values())
                        .ifPresent(usedParameters::remove);
            }

            tabSupportData.setTabReturn(EnumUtilities.getPrettyNames(usedParameters.toArray(new Enum[0])));
        }

        final Optional<MobSpawnParameter> mobSpawnParameterOpt = EnumUtilities.getIgnoreCase(String.valueOf(lastArg.charAt(0)), MobSpawnParameter.values());
        if (!mobSpawnParameterOpt.isPresent()) {
            return;
        }

        tabSupportData.setTabReturn(DataUtilities.combine(lastArg, this.getStartWithIgnoreCase(mobSpawnParameterOpt.get().getValues(), lastArg.substring(1))));
    }

    private void handleGadgetOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            final List<String> values = new ArrayList<>(Arrays.asList(BukkitValues.getCosmetics()));
            values.addAll(Arrays.asList("clear", "list"));

            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(values, args[2]));
        }
    }

    private void handleTempGadgetOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(BukkitValues.getCosmetics(), args[2]));
        }
    }

    private void handleScoreboardOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(SCOREBOARD_LINES, args[2]));
        }

        // TODO: Rewrite
        final String lastArg = args[args.length - 1];
        final String lastChar = lastArg.length() > 1 ? String.valueOf(lastArg.charAt(lastArg.length() - 1)).toLowerCase() : "";
        final String lastArgUse = lastArg.length() > 1 ? lastArg.substring(0, lastArg.length() - 1) : "";

        final List<String> options = new ArrayList<>();
        if ("&".equals(lastChar) || "".equals(lastChar)) {
            for (final String formation : SCOREBOARD_CHAT_FORMATION_OPTIONS) {
                if (formation.startsWith(lastChar)) {
                    options.add(lastArgUse + formation);
                }
            }
        }
        tabSupportData.setTabReturn(options);
    }

    private void handleDamageOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            tabSupportData.setTabReturn(DataUtilities.getStartWithIgnoreCase(EnumUtilities.getPrettyNames(DamageParameter.values()), args[2]));
        }
    }

    private void handleHealthOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(SUGGESTED_HEALTH, args[2]));
        }
    }

    private void handleHungerOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(SUGGESTED_FOOD, args[2]));
        }
    }

    private void handleItemOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(Collections.singletonList("drop"), args[2]));
        }
    }

    private void handleBlockInteractionOptions(final TabSupportData tabSupportData, final String[] args) {
        switch (args.length) {
            case 3:
                tabSupportData.setTabReturn(this.getStartWithIgnoreCase(Collections.singletonList("whitelist"), args[2]));
                return;
            case 4:
                tabSupportData.setTabReturn(this.getStartWithIgnoreCase(EnumUtilities.getPrettyNames(BlockSettingParameter.values()), args[2]));
                return;
            default:
        }
    }

    private void handleEffectOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            final List<String> values = new ArrayList<>(this.getAllPlayersInLobby());
            values.add("all");

            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(values, args[2]));
            return;
        } else if (args.length == 4) {
            final List<String> values = new ArrayList<>(Arrays.asList(BukkitValues.getEffects()));
            values.add(EnumUtilities.getPrettyName(EffectParameter.CLEAR));

            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(values, args[3]));
            return;
        }

        final Optional<EffectParameter> effectParameterOpt = EnumUtilities.getIgnoreCase(args[3], EffectParameter.values());
        if (!effectParameterOpt.isPresent() || effectParameterOpt.get() != EffectParameter.CLEAR) {
            if (args.length == 5) {
                tabSupportData.setTabReturn(this.getStartWithIgnoreCase(SUGGESTED_EFFECT_LEVEL, args[4]));
            } else if (args.length == 6) {
                tabSupportData.setTabReturn(this.getStartWithIgnoreCase(SUGGESTED_EFFECT_TIME, args[5]));
            }
        }
    }

    private void handleGamekitOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(this.getAllPlayersInLobby(), args[2]));
        } else if (args.length == 4 && McMod.getModCache().getCurrentGame() != null) {
            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(McMod.getModCache().getCurrentGame().getKits(), args[3]));
        }
    }

    private void handleKitOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            tabSupportData.setTabReturn(DataUtilities.getStartWithIgnoreCase(EnumUtilities.getPrettyNames(KitParameter.values()), args[2]));
        }
    }

    private void handleClearOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            final List<String> values = new ArrayList<>(this.getAllPlayersInLobby());
            values.add("@a");

            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(values, args[2]));
        }
    }

    private void handleTpOptions(final TabSupportData tabSupportData, final String[] args) {
        if (args.length == 3) {
            final List<String> values = new ArrayList<>(this.getAllPlayersInLobby());
            values.addAll(EnumUtilities.getPrettyNames(TeleportParameter.values()));

            tabSupportData.setTabReturn(this.getStartWithIgnoreCase(values, args[2]));
        } else if (args.length == 4) {
            final Optional<TeleportParameter> parameterOpt = EnumUtilities.getIgnoreCase(args[2], TeleportParameter.values());
            if (parameterOpt.isPresent() && parameterOpt.get() == TeleportParameter.HERE) {
                final List<String> values = new ArrayList<>(this.getAllPlayersInLobby());
                values.add("all");

                tabSupportData.setTabReturn(this.getStartWithIgnoreCase(values, args[3]));
            }
        }
    }

    private enum FirstParameter {
        BRIDGE,
        SMASH,
        DM,
        SUPPLY_DROP,
        ANNOUNCE,
        SILENCE,
        SETTINGS,
        BLOCK_PLACE_CREATIVE,
        BLOCK_BREAK_CREATIVE,
        MOB_GRIEFING,
        AREA,
        GM,
        DOUBLE_JUMP,
        MOB,
        RADIUS,
        GADGET,
        TEMP_GADGET,
        SCOREBOARD,
        ADMIN,
        DAMAGE,
        HEALTH,
        HUNGER,
        ITEM,
        BLOCK_BREAK,
        BLOCK_PLACE,
        EFFECT,
        GAMEKIT,
        KIT,
        CLEAR,
        GIVE,
        REVIVE,
        STACKER,
        TP
    }

    private enum DamageParameter {
        ALL,
        PVP,
        PVE,
        FALL
    }

    private enum KitParameter {
        SET,
        APPLY,
        CLEAR
    }

    private enum BlockSettingParameter {
        ADD,
        REMOVE,
        LIST,
        CLEAR
    }

    private enum EffectParameter {
        CLEAR
    }

    private enum MobParameter {
        KILL
    }

    @Getter
    private enum MobSpawnParameter {
        S("1", "2", "3", "4", "5", "10", "20", "100", "200"),
        I(BukkitValues.getMaterials()),
        E(BukkitValues.getEffects()),
        A("Leather", "Chain", "Gold", "Iron", "Diamond"),
        H("50", "100", "200", "400", "500", "900", "10000", "10000000");

        private final String[] values;

        MobSpawnParameter(final String... values) {
            this.values = values;
        }
    }

    private enum TeleportParameter {
        HERE,
        ALL
    }
}
