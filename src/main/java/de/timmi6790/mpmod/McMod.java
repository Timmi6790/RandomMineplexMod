package de.timmi6790.mpmod;

import de.timmi6790.mpmod.command.CommandManager;
import de.timmi6790.mpmod.listeners.UpdateChecker;
import de.timmi6790.mpmod.listeners.events.EventListener;
import de.timmi6790.mpmod.listeners.events.MineplexEventListener;
import de.timmi6790.mpmod.modules.AbstractModule;
import de.timmi6790.mpmod.tabsupport.TabSupportManager;
import de.timmi6790.mpmod.utilities.EventUtilities;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.reflections.Reflections;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Mod(
        modid = Reference.MODID,
        name = Reference.NAME,
        version = Reference.VERSION,
        acceptedMinecraftVersions = "1.8.9",
        guiFactory = "de.timmi6790.mpmod.gui.GuiFactory",
        clientSideOnly = true
)
@Log4j2
@Getter
@Setter
public class McMod {
    @Getter
    @Mod.Instance(value = Reference.MODID)
    private static McMod instance;

    private final Map<Class<? extends AbstractModule>, AbstractModule> modules = new HashMap<>();

    private final ModCache modCache = new ModCache();
    private final TabSupportManager tabSupportManager = new TabSupportManager();
    private final CommandManager commandManager = new CommandManager();
    private Configuration configuration;
    private String configDirectory;

    private final String modId = Reference.MODID;
    private final String modName = Reference.NAME;
    private final String versionUrl = Reference.VERSION_URL;
    private final String version = Reference.VERSION;
    private final String downloadUrl = Reference.DOWNLOAD_URL;

    private void loadModules() {
        final Reflections reflections = new Reflections("de.timmi6790");
        final Set<Class<? extends AbstractModule>> modules = reflections.getSubTypesOf(AbstractModule.class);
        for (final Class<? extends AbstractModule> module : modules) {
            try {
                this.addModules(module.getConstructor(McMod.class).newInstance(this));
            } catch (final Exception e) {
                log.error("Trying to initialize {}", module, e);
            }
        }
    }

    public <T extends AbstractModule> Optional<T> getModule(final Class<T> clazz) {
        return (Optional<T>) Optional.ofNullable(this.modules.get(clazz));
    }

    public <T extends AbstractModule> T getModuleOrThrow(final Class<T> clazz) {
        return this.getModule(clazz).orElseThrow(RuntimeException::new);
    }

    protected void addModules(final AbstractModule... modules) {
        for (final AbstractModule module : modules) {
            this.modules.put(module.getClass(), module);
        }
    }


    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        this.configDirectory = event.getModConfigurationDirectory().toString();
        final File path = new File(this.configDirectory + File.separator + this.getModId() + ".cfg");
        this.configuration = new Configuration(path);

        this.loadModules();

        for (final AbstractModule module : this.getModules().values()) {
            log.info("PreInnit module {}", module.getName());
            module.preInit(event);
        }
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        for (final AbstractModule module : this.getModules().values()) {
            log.info("Innit module {}", module.getName());
            module.init(event);
        }

        EventUtilities.registerEvents(
                new MineplexEventListener(this),
                new EventListener(),
                new UpdateChecker(this)
        );
    }
}
