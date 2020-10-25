package de.timmi6790.mpmod.utilities;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@UtilityClass
public class PlayerUtilities {
    private final AsyncLoadingCache<UUID, Optional<String>> playerNameCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .buildAsync(uuid -> {
                try {
                    final URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + PlayerUtilities.uuidShorter(uuid));
                    final URLConnection con = url.openConnection();

                    final JsonElement element = new JsonParser().parse(new InputStreamReader(
                            con.getInputStream(),
                            StandardCharsets.UTF_8
                    ));

                    return Optional.of(element.getAsJsonObject().get("name").getAsString());
                } catch (final Exception ignore) {
                    return Optional.empty();
                }
            });

    @SneakyThrows
    public Optional<String> uuidToName(final UUID uuid) {
        final CompletableFuture<Optional<String>> nameFuture = playerNameCache.get(uuid);
        try {
            return nameFuture.get(20, TimeUnit.SECONDS);
        } catch (final ExecutionException | TimeoutException e) {
            return Optional.empty();
        }
    }

    private String uuidShorter(final UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public Set<String> getAllPlayersInLobby() {
        final Set<String> playersInLobby = new HashSet<>();
        // Nethandler Players
        for (final NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            playersInLobby.add(playerInfo.getGameProfile().getName());
        }

        return playersInLobby;
    }
}
