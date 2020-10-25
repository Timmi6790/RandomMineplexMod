package de.timmi6790.mpmod.modules.community;

import de.timmi6790.mpmod.McMod;
import de.timmi6790.mpmod.modules.AbstractModule;
import de.timmi6790.mpmod.modules.community.commands.BarrierCommand;
import de.timmi6790.mpmod.modules.community.fixes.ClansTexturePackDenyFix;
import de.timmi6790.mpmod.modules.community.fixes.CrashPotionFix;
import de.timmi6790.mpmod.modules.community.tabsupport.ImmortalGiveTabSupport;
import de.timmi6790.mpmod.modules.community.tabsupport.TabSupportEventCommand;
import de.timmi6790.mpmod.tabsupport.tabsupports.PlayerNamesOverrideTabSupport;
import lombok.Getter;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Getter
public class CommunityModule extends AbstractModule {
    private final CommunityCache cache = new CommunityCache();
    private CommunityConfig config;

    public CommunityModule(final McMod mod) {
        super(mod, "Community");
    }

    @Override
    public void preInit(final FMLPreInitializationEvent event) {
        this.config = new CommunityConfig(this, this.getMod().getConfiguration());
    }

    @Override
    public void init(final FMLInitializationEvent event) {
        this.registerEvents(
                this,
                new CrashPotionFix(),
                new ClansTexturePackDenyFix(this),
                this.config
        );

        this.getMod().getCommandManager().addCommands(
                new BarrierCommand(this)
        );

        this.getMod().getTabSupportManager().registerTabSupports(
                new PlayerNamesOverrideTabSupport(
                        "/stats",
                        "/party",
                        "/z"
                ),
                new TabSupportEventCommand(this.getMod().getModCache()),
                new ImmortalGiveTabSupport()
        );
    }
}
