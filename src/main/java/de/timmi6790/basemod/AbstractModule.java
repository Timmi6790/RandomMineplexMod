package de.timmi6790.basemod;


import de.timmi6790.basemod.command.AbstractCommand;
import de.timmi6790.basemod.tabsupport.AbstractTabSupport;
import de.timmi6790.basemod.utilities.EventUtilities;
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

    public void registerCommands(final AbstractCommand... commands) {
        this.mod.getCommandManager().addCommands(commands);
    }

    public void registerTabSupports(final AbstractTabSupport... tabSupports) {
        this.mod.getTabSupportManager().registerTabSupports(tabSupports);
    }

    public abstract void preInit(final FMLPreInitializationEvent event);

    public abstract void init(final FMLInitializationEvent event);
}
