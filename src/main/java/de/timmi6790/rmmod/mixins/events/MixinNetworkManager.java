package de.timmi6790.rmmod.mixins.events;

import de.timmi6790.basemod.events.PacketReceiveEvent;
import de.timmi6790.basemod.events.PacketSendEvent;
import de.timmi6790.basemod.utilities.EventUtilities;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    @Shadow
    private INetHandler packetListener;

    @Inject(method = "dispatchPacket", at = @At("HEAD"), cancellable = true)
    private void sendPacketPre(final Packet dispatchPacket1,
                               final GenericFutureListener<? extends Future<? super Void>>[] dispatchPacket2,
                               final CallbackInfo info) {
        final PacketSendEvent.Pre packetEvent = new PacketSendEvent.Pre(dispatchPacket1, dispatchPacket2);
        EventUtilities.postEventSave(packetEvent);

        if (packetEvent.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "dispatchPacket", at = @At("RETURN"))
    private void sendPacketPost(final Packet dispatchPacket1,
                                final GenericFutureListener<? extends Future<? super Void>>[] dispatchPacket2,
                                final CallbackInfo info) {
        final PacketSendEvent.Post packetEvent = new PacketSendEvent.Post(dispatchPacket1, dispatchPacket2);
        EventUtilities.postEventSave(packetEvent);
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void receivePacketPre(final ChannelHandlerContext channelRead1,
                                  final Packet channelRead2,
                                  final CallbackInfo info) {
        final PacketReceiveEvent.Pre packetReceiveEventPre = new PacketReceiveEvent.Pre(this.packetListener, channelRead2, channelRead1);
        EventUtilities.postEventSave(packetReceiveEventPre);

        if (packetReceiveEventPre.isCanceled()) {
            info.cancel();
        }
    }

    @Inject(method = "channelRead0", at = @At("RETURN"))
    private void receivePacketPost(final ChannelHandlerContext channelRead1,
                                   final Packet channelRead2,
                                   final CallbackInfo info) {
        // Run event in main thread
        final PacketReceiveEvent.Post packetReceiveEventPost = new PacketReceiveEvent.Post(this.packetListener, channelRead2, channelRead1);
        this.executorService.submit(() -> EventUtilities.postEventSave(packetReceiveEventPost));
    }
}