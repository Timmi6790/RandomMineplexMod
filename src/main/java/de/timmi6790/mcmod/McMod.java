package de.timmi6790.mcmod;

import de.timmi6790.mcmod.command.CommandManager;
import de.timmi6790.mcmod.datatypes.TaskScheduler;
import de.timmi6790.mcmod.listeners.events.EventListener;
import de.timmi6790.mcmod.listeners.events.MineplexEventListener;
import de.timmi6790.mcmod.modules.AbstractModule;
import de.timmi6790.mcmod.modules.community.CommunityModule;
import de.timmi6790.mcmod.tabsupport.TabSupportManager;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Mod(
        modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION,
        acceptedMinecraftVersions = Reference.MC_VERSION,
        guiFactory = Reference.GUI_FACTORY_CLASS, clientSideOnly = true
)
public class McMod {
    private static final Map<String, AbstractModule> modules = new HashMap<>();
    @Getter
    private static final ModCache modCache = new ModCache();
    @Getter
    private static final TaskScheduler taskScheduler = new TaskScheduler();
    @Getter
    private static final TabSupportManager tabSupportManager = new TabSupportManager();
    @Getter
    private static final CommandManager commandManager = new CommandManager();
    @Getter
    private static Configuration configuration;
    private static String configDirectory;

    private static void addModules(final AbstractModule... modules) {
        for (final AbstractModule module : modules) {
            McMod.modules.put(module.getName().toLowerCase(), module);
        }
    }

    public static Optional<AbstractModule> getModule(final String name) {
        return Optional.ofNullable(modules.get(name.toLowerCase()));
    }

    public static AbstractModule getModuleOrThrow(final String name) {
        return getModule(name).orElseThrow(RuntimeException::new);
    }

    public static void registerEvents(final Object... events) {
        for (final Object event : events) {
            MinecraftForge.EVENT_BUS.register(event);
        }
    }

    public static void unRegisterEvents(final Object... events) {
        for (final Object event : events) {
            MinecraftForge.EVENT_BUS.unregister(event);
        }
    }

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        McMod.addModules(
                new CommunityModule()
        );

        configDirectory = event.getModConfigurationDirectory().toString();
        if (configuration == null) {
            final File path = new File(configDirectory + "/" + Reference.MODID + ".cfg");
            configuration = new Configuration(path);
        }

        for (final AbstractModule module : modules.values()) {
            System.out.printf("PreInnit module %s%n", module.getName());
            module.preInit(event);
        }
    }

    @EventHandler
    public void init(final FMLInitializationEvent event) {
        for (final AbstractModule module : modules.values()) {
            System.out.printf("Innit module %s%n", module.getName());
            module.init(event);
        }

        registerEvents(
                this,
                tabSupportManager,
                taskScheduler,
                commandManager,
                new MineplexEventListener(),
                new EventListener()
        );
    }
}
