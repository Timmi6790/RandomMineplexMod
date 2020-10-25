package de.timmi6790.mpmod.modules.community.fixes;

import de.timmi6790.mpmod.events.PacketSendEvent;
import de.timmi6790.mpmod.modules.community.CommunityModule;
import de.timmi6790.mpmod.utilities.MessageBuilder;
import lombok.Data;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Data
public class ClansTexturePackDenyFix {
    private final CommunityModule communityModule;

    @SubscribeEvent
    public void fakeResourcePackStatus(final PacketSendEvent.Pre event) {
        if (!(event.getPacket() instanceof C19PacketResourcePackStatus)
                || !this.communityModule.getConfig().isFakeAcceptTexturePack()) {
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
