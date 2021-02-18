package de.timmi6790.basemod.utilities;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class PlayerUtilities {
    public Set<String> getAllPlayersInLobby() {
        final Set<String> playersInLobby = new HashSet<>();
        // Nethandler Players
        for (final NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            playersInLobby.add(playerInfo.getGameProfile().getName());
        }

        return playersInLobby;
    }
}
