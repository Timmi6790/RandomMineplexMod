package de.timmi6790.mpmod;

import de.timmi6790.mpmod.command.CommandManager;
import de.timmi6790.mpmod.listeners.events.EventListener;
import de.timmi6790.mpmod.listeners.events.MineplexEventListener;
import de.timmi6790.mpmod.modules.AbstractModule;
import de.timmi6790.mpmod.modules.community.CommunityModule;
import de.timmi6790.mpmod.tabsupport.TabSupportManager;
import de.timmi6790.mpmod.utilities.TaskScheduler;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
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
        modid = Reference.MODID,
        name = Reference.NAME,
        version = Reference.VERSION,
        acceptedMinecraftVersions = Reference.MC_VERSION,
        guiFactory = Reference.GUI_FACTORY_CLASS,
        clientSideOnly = true
)
@Log4j2
public class McMod {
    private static final Map<Class<? extends AbstractModule>, AbstractModule> modules = new HashMap<>();
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
            McMod.modules.put(module.getClass(), module);
        }
    }

    public static <T extends AbstractModule> Optional<T> getModule(final Class<T> clazz) {
        return (Optional<T>) Optional.ofNullable(modules.get(clazz));
    }

    public static <T extends AbstractModule> T getModuleOrThrow(final Class<T> clazz) {
        return getModule(clazz).orElseThrow(RuntimeException::new);
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
        addModules(
                new CommunityModule()
        );

        configDirectory = event.getModConfigurationDirectory().toString();
        if (configuration == null) {
            final File path = new File(configDirectory + "/" + Reference.MODID + ".cfg");
            configuration = new Configuration(path);
        }

        for (final AbstractModule module : McMod.modules.values()) {
            log.info("PreInnit module {}", module.getName());
            module.preInit(event);
        }
    }

    @EventHandler
    public void init(final FMLInitializationEvent event) {
        for (final AbstractModule module : modules.values()) {
            log.info("Innit module {}", module.getName());
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
