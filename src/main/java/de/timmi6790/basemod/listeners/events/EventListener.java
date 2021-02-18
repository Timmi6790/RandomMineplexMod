package de.timmi6790.basemod.listeners.events;

import de.timmi6790.basemod.events.PacketReceiveEvent;
import de.timmi6790.basemod.events.chat.ChatReceiveEvent;
import de.timmi6790.basemod.utilities.EventUtilities;
import net.minecraft.network.play.server.S02PacketChat;
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
                EventUtilities.postEventSave(chatReceiveEvent);

                if (chatReceiveEvent.isCanceled()) {
                    event.setCanceled(true);
                } else {
                    chatEvent.chatComponent = chatReceiveEvent.getMessage();
                }
                break;
            default:
        }
    }
}
