package de.timmi6790.mcmod.events;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@EqualsAndHashCode(callSuper = true)
@Data
public class PacketSendEvent extends Event {
    private final GenericFutureListener<? extends Future<? super Void>>[] returnListener;
    private Packet packet;

    public PacketSendEvent(final GenericFutureListener<? extends Future<? super Void>>[] returnListener, final Packet packet) {
        this.returnListener = returnListener != null ? returnListener.clone() : null;
        this.packet = packet;
    }

    public GenericFutureListener<? extends Future<? super Void>>[] getReturnListener() {
        return this.returnListener.clone();
    }

    @Cancelable
    public static class Pre extends PacketSendEvent {
        public Pre(final Packet packet, final GenericFutureListener<? extends Future<? super Void>>[] returnListener) {
            super(returnListener, packet);
        }
    }

    public static class Post extends PacketSendEvent {
        public Post(final Packet packet, final GenericFutureListener<? extends Future<? super Void>>[] returnListener) {
            super(returnListener, packet);
        }
    }
}
