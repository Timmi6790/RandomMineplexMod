package de.timmi6790.mpmod.listeners.events;

import de.timmi6790.mpmod.McMod;
import de.timmi6790.mpmod.ModCache;
import de.timmi6790.mpmod.events.PacketReceiveEvent;
import de.timmi6790.mpmod.events.mineplex.MineplexServerChangeEvent;
import de.timmi6790.mpmod.events.mineplex.MineplexServerJoinEvent;
import de.timmi6790.mpmod.utilities.EnumUtilities;
import de.timmi6790.mpmod.utilities.EventUtilities;
import de.timmi6790.mpmod.utilities.StringUtilities;
import de.timmi6790.mpmod.values.MineplexGames;
import lombok.Data;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class MineplexEventListener {
    private static final Pattern MINEPLEX_SERVER_PATTERN = Pattern.compile("^Mineplex Network {3}((.*)-\\d*)$");

    private final McMod mod;

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void detectGameOnServerChange(final PacketReceiveEvent.Post event) {
        if (!(event.getPacket() instanceof S47PacketPlayerListHeaderFooter)) {
            return;
        }

        final String header = ((S47PacketPlayerListHeaderFooter) event.getPacket()).getHeader().getUnformattedText();
        final ModCache cache = this.mod.getModCache();
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

        if (!cache.isOnMineplex()) {
            cache.setOnMineplex(true);
            final MineplexServerJoinEvent joinEvent = new MineplexServerJoinEvent(fullServerName);
            EventUtilities.postEventSave(joinEvent);

        } else {
            final MineplexServerChangeEvent serverChangeEvent = new MineplexServerChangeEvent(
                    cache.getCurrentServer(),
                    fullServerName
            );
            EventUtilities.postEventSave(serverChangeEvent);
        }

        final MineplexGames game = MineplexGames.getGameByName(serverName).orElse(null);
        cache.setCurrentServer(fullServerName)
                .setCurrentGame(game)
                .setOnMps(game == null);
    }

    @SubscribeEvent
    public void onServerLeave(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        this.mod.getModCache()
                .setOnMineplex(false)
                .setCurrentServer(null)
                .setCurrentGame(null)
                .setOnMps(false);
    }
}

