package de.timmi6790.clansMod.mixins.client;

import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(C19PacketResourcePackStatus.class)
public class MixinC19PacketResourcePackStatus {

    @Shadow
    private C19PacketResourcePackStatus.Action status;

    @Inject(method = "writePacketData", at = @At("HEAD"))
    private void fakeTpAccept(final CallbackInfo info) {
        // Sending the Loaded status is enough for the server. We don't need to send the Accept status first .... strange server
        if (this.status == C19PacketResourcePackStatus.Action.DECLINED) {
            this.status = C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED;
        }
    }
}
