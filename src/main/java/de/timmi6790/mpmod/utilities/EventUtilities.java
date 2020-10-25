package de.timmi6790.mpmod.utilities;

import lombok.experimental.UtilityClass;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

@UtilityClass
public class EventUtilities {
    public void postEventSave(final Event event) {
        try {
            MinecraftForge.EVENT_BUS.post(event);
        } catch (final Exception ignore) {
        }
    }

    public void registerEvents(final Object... events) {
        for (final Object event : events) {
            MinecraftForge.EVENT_BUS.register(event);
        }
    }

    public void unRegisterEvents(final Object... events) {
        for (final Object event : events) {
            MinecraftForge.EVENT_BUS.unregister(event);
        }
    }
}
