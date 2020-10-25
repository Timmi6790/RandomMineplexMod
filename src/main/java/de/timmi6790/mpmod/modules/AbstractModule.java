package de.timmi6790.mpmod.modules;


import de.timmi6790.mpmod.McMod;
import de.timmi6790.mpmod.utilities.EventUtilities;
import lombok.Data;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Data
public abstract class AbstractModule {
    private final McMod mod;
    private final String name;

    public void registerEvents(final Object... events) {
        EventUtilities.registerEvents(events);
    }

    public abstract void preInit(final FMLPreInitializationEvent event);

    public abstract void init(final FMLInitializationEvent event);
}
