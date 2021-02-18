package de.timmi6790.rmmod.modules.community;

import de.timmi6790.basemod.AbstractModule;
import de.timmi6790.basemod.McMod;
import de.timmi6790.basemod.tabsupport.tabsupports.PlayerNamesTabSupport;
import de.timmi6790.rmmod.modules.community.commands.BarrierCommand;
import de.timmi6790.rmmod.modules.community.fixes.ClansTexturePackDenyFix;
import de.timmi6790.rmmod.modules.community.fixes.CrashPotionFix;
import de.timmi6790.rmmod.modules.community.tabsupport.EventCommandTabSupport;
import de.timmi6790.rmmod.modules.community.tabsupport.ImmortalGiveTabSupport;
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
                new PlayerNamesTabSupport(
                        "/stats",
                        "/party",
                        "/z"
                ),
                new EventCommandTabSupport(this.getMod().getModCache()),
                new ImmortalGiveTabSupport()
        );
    }
}
