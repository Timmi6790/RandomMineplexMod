package de.timmi6790.mcmod.modules;


import de.timmi6790.mcmod.McMod;
import lombok.Data;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Data
public abstract class AbstractModule {
    private final String name;

    public AbstractModule(final String name) {
        this.name = name;
    }

    public static void registerEvents(final Object... events) {
        McMod.registerEvents(events);
    }

    public abstract void preInit(final FMLPreInitializationEvent event);

    public abstract void init(final FMLInitializationEvent event);
}
