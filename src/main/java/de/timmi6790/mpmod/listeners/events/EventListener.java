package de.timmi6790.mpmod.listeners.events;

import de.timmi6790.mpmod.events.PacketReceiveEvent;
import de.timmi6790.mpmod.events.chat.ActionBarReceiveOldEvent;
import de.timmi6790.mpmod.events.chat.ChatReceiveEvent;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventListener {
    @SubscribeEvent
    public void events(final PacketReceiveEvent.Pre event) {
        if (!(event.getPacket() instanceof S02PacketChat)) {
            return;
        }

        final S02PacketChat chatEvent = (S02PacketChat) event.getPacket();
        switch (chatEvent.getType()) {
            case 0:
            case 1:
                // Chat
                final ChatReceiveEvent chatReceiveEvent = new ChatReceiveEvent(chatEvent.getChatComponent());
                try {
                    MinecraftForge.EVENT_BUS.post(chatReceiveEvent);
                } catch (final Exception ignore) {
                }

                if (chatReceiveEvent.isCanceled()) {
                    event.setCanceled(true);
                } else {
                    chatEvent.chatComponent = chatReceiveEvent.getMessage();
                }
                break;
            case 2:
                // Old actionbar
                final ActionBarReceiveOldEvent actionBarReceiveOldEvent = new ActionBarReceiveOldEvent(chatEvent.getChatComponent());
                MinecraftForge.EVENT_BUS.post(actionBarReceiveOldEvent);

                if (actionBarReceiveOldEvent.isCanceled()) {
                    event.setCanceled(true);
                } else {
                    chatEvent.chatComponent = actionBarReceiveOldEvent.getMessage();
                }
                break;
            default:
        }
    }
}
