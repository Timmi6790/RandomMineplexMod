package de.timmi6790.mpmod.utilities;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.Packet;

import java.util.Optional;

@UtilityClass
public class Shortcuts {
    public Optional<EntityPlayerSP> getPlayer() {
        return Optional.ofNullable(Minecraft.getMinecraft().thePlayer);
    }

    public Optional<WorldClient> getWorld() {
        return Optional.ofNullable(Minecraft.getMinecraft().theWorld);
    }

    public void sendPacket(final Packet packet) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(packet);
    }
}
