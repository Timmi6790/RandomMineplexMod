package de.timmi6790.mpmod.modules.community.fixes;

import de.timmi6790.mpmod.events.PacketReceiveEvent;
import de.timmi6790.mpmod.utilities.MessageBuilder;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CrashPotionFix {
    @SubscribeEvent
    public void onPotionPacket(final PacketReceiveEvent.Pre event) {
        final Packet packet = event.getPacket();

        if (packet instanceof S1DPacketEntityEffect) {
            // Check if the server is sending an invalid potion effect that could crash the client
            final S1DPacketEntityEffect entityEffect = (S1DPacketEntityEffect) packet;
            final int potionId = (entityEffect.getEffectId() & 0xff);
            if (potionId > Potion.potionTypes.length || Potion.potionTypes[potionId] == null) {
                new MessageBuilder("AntiCrash> ", EnumChatFormatting.BLUE)
                        .addMessage("Canceled potion packet. PotionID:", EnumChatFormatting.GRAY)
                        .addMessage(String.valueOf(potionId), EnumChatFormatting.YELLOW)
                        .sendToPlayer();
                event.setCanceled(true);
            }
        }
    }
}
