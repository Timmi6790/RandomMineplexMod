package de.timmi6790.mcmod.modules.community.fixes;

import de.timmi6790.mcmod.McMod;
import de.timmi6790.mcmod.datatypes.MessageBuilder;
import de.timmi6790.mcmod.events.PacketSendEvent;
import de.timmi6790.mcmod.modules.community.CommunityModule;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClansTexturePackDenyFix {
    @SubscribeEvent
    public void fakeResourcePackStatus(final PacketSendEvent.Pre event) {
        if (!(event.getPacket() instanceof C19PacketResourcePackStatus) || !((CommunityModule) McMod.getModuleOrThrow("Community")).getConfig().isFakeAcceptTexturePack()) {
            return;
        }

        final C19PacketResourcePackStatus packetResourcePackStatus = (C19PacketResourcePackStatus) event.getPacket();
        if (packetResourcePackStatus.status != C19PacketResourcePackStatus.Action.DECLINED) {
            return;
        }

        packetResourcePackStatus.status = C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED;
        new MessageBuilder("TPBypass> ", EnumChatFormatting.BLUE)
                .addMessage("Faked texturepack accept.", EnumChatFormatting.GRAY)
                .sendToPlayerDelayed(1);
    }
}
