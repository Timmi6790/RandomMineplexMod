package de.timmi6790.rmmod.modules.community;

import de.timmi6790.basemod.McMod;
import lombok.Data;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Data
public class CommunityConfig {
    private final CommunityModule communityModule;
    private final Configuration configuration;

    private boolean showBarrierBlocks;
    private boolean fakeAcceptTexturePack;

    public CommunityConfig(final CommunityModule communityModule, final Configuration configuration) {
        this.communityModule = communityModule;
        this.configuration = configuration;

        this.loadConfiguration();
    }

    public void loadConfiguration() {
        this.showBarrierBlocks = this.configuration.getBoolean(
                "showBarrierBlocks",
                Configuration.CATEGORY_GENERAL,
                false,
                ""
        );
        this.fakeAcceptTexturePack = this.configuration.getBoolean(
                "fakeAcceptTexturePack",
                Configuration.CATEGORY_GENERAL,
                true,
                ""
        );

        final CommunityCache cache = this.communityModule.getCache();
        cache.setShowBarrier(this.showBarrierBlocks);

        if (this.configuration.hasChanged()) {
            this.configuration.save();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangeEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(McMod.getInstance().getModId())) {
            this.loadConfiguration();
        }
    }
}
