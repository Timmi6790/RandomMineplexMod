package de.timmi6790.mcmod.listeners.events;

import de.timmi6790.mcmod.McMod;
import de.timmi6790.mcmod.ModCache;
import de.timmi6790.mcmod.events.PacketReceiveEvent;
import de.timmi6790.mcmod.events.mineplex.MineplexServerChangeEvent;
import de.timmi6790.mcmod.events.mineplex.MineplexServerJoinEvent;
import de.timmi6790.mcmod.utilities.EnumUtilities;
import de.timmi6790.mcmod.utilities.StringUtilities;
import de.timmi6790.mcmod.values.MineplexGames;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MineplexEventListener {
    private static final Pattern MINEPLEX_SERVER_PATTERN = Pattern.compile("^Mineplex Network {3}((.*)-\\d*)$");

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void detectGameOnServerChange(final PacketReceiveEvent.Post event) {
        if (!(event.getPacket() instanceof S47PacketPlayerListHeaderFooter)) {
            return;
        }

        final String header = ((S47PacketPlayerListHeaderFooter) event.getPacket()).getHeader().getUnformattedText();
        final ModCache cache = McMod.getModCache();
        // Mps based game change detection
        if (cache.isOnMineplex()) {
            final Optional<MineplexGames> gameOpt = EnumUtilities.getIgnoreCase(
                    StringUtilities.removeColourCodes(header).replace(" ", ""),
                    MineplexGames.values()
            );
            if (gameOpt.isPresent()) {
                cache.setCurrentGame(gameOpt.get());
                return;
            }
        }

        final Matcher serverMatcher = MINEPLEX_SERVER_PATTERN.matcher(StringUtilities.removeColourCodes(header));
        if (!serverMatcher.find()) {
            return;
        }

        final String fullServerName = serverMatcher.group(1);
        final String serverName = serverMatcher.group(2);

        final String oldServerName = cache.getCurrentServer();
        if (!cache.isOnMineplex()) {
            cache.setOnMineplex(true);
            final MineplexServerJoinEvent joinEvent = new MineplexServerJoinEvent(fullServerName);
            MinecraftForge.EVENT_BUS.post(joinEvent);

        } else {
            final MineplexServerChangeEvent serverChangeEvent = new MineplexServerChangeEvent(oldServerName, fullServerName);
            MinecraftForge.EVENT_BUS.post(serverChangeEvent);
        }

        cache.setCurrentServer(fullServerName);

        final MineplexGames game = MineplexGames.getGameByName(serverName).orElse(null);
        cache.setCurrentGame(game);
        cache.setOnMps(game == null);
    }

    @SubscribeEvent
    public void onServerLeave(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        final ModCache cache = McMod.getModCache();
        cache.setOnMineplex(false);
        cache.setCurrentServer(null);
        cache.setCurrentGame(null);
        cache.setOnMps(false);
    }
}

