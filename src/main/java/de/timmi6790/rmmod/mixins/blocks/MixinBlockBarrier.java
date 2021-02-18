package de.timmi6790.rmmod.mixins.blocks;

import de.timmi6790.basemod.McMod;
import de.timmi6790.rmmod.modules.community.CommunityCache;
import de.timmi6790.rmmod.modules.community.CommunityModule;
import lombok.Getter;
import net.minecraft.block.BlockBarrier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockBarrier.class)
public class MixinBlockBarrier {
    @Getter(lazy = true)
    private final CommunityCache communityCache = McMod.getInstance()
            .getModuleOrThrow(CommunityModule.class)
            .getCache();

    /**
     * @author
     */
    @Overwrite
    public int getRenderType() {
        if (this.getCommunityCache().isShowBarrier()) {
            return 3;
        }
        return -1;
    }
}
