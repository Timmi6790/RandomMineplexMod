package de.timmi6790.basemod.events;

import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PacketReceiveEvent extends Event {
    private final INetHandler packetListener;
    private final ChannelHandlerContext channelHandlerContext;
    private Packet packet;

    @Cancelable
    public static class Pre extends PacketReceiveEvent {
        public Pre(final INetHandler packetListener,
                   final Packet packet,
                   final ChannelHandlerContext channelHandlerContext) {
            super(packetListener, channelHandlerContext, packet);
        }
    }

    public static class Post extends PacketReceiveEvent {
        public Post(final INetHandler packetListener,
                    final Packet packet,
                    final ChannelHandlerContext channelHandlerContext) {
            super(packetListener, channelHandlerContext, packet);
        }
    }
}

