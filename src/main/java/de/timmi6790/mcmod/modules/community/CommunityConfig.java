package de.timmi6790.mcmod.modules.community;

import de.timmi6790.mcmod.McMod;
import de.timmi6790.mcmod.Reference;
import lombok.Data;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Data
public class CommunityConfig {
    private final Configuration configuration;

    private boolean showBarrierBlocks;
    private boolean fakeAcceptTexturePack;

    public CommunityConfig(final Configuration configuration) {
        this.configuration = configuration;

        this.loadConfiguration();
    }

    public void loadConfiguration() {
        this.showBarrierBlocks = this.configuration.getBoolean("showBarrierBlocks", Configuration.CATEGORY_GENERAL, false, "");
        this.fakeAcceptTexturePack = this.configuration.getBoolean("fakeAcceptTexturePack", Configuration.CATEGORY_GENERAL, true, "");

        final CommunityCache cache = McMod.getModuleOrThrow(CommunityModule.class).getCache();
        cache.setShowBarrier(this.showBarrierBlocks);

        if (this.configuration.hasChanged()) {
            this.configuration.save();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangeEvent(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Reference.MODID)) {
            this.loadConfiguration();
        }
    }
}
