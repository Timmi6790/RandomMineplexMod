package de.timmi6790.basemod;

import de.timmi6790.basemod.command.CommandManager;
import de.timmi6790.basemod.listeners.events.EventListener;
import de.timmi6790.basemod.listeners.events.MineplexEventListener;
import de.timmi6790.basemod.tabsupport.TabSupportManager;
import de.timmi6790.basemod.update_checker.UpdateChecker;
import de.timmi6790.basemod.utilities.EventUtilities;
import de.timmi6790.rmmod.modules.community.CommunityModule;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
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
        acceptedMinecraftVersions = "1.8.9",
        guiFactory = "de.timmi6790.basemod.gui.GuiFactory",
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

        this.addModules(
                new CommunityModule(this)
        );

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
