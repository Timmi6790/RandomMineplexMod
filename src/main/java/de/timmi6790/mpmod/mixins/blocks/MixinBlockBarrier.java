package de.timmi6790.mpmod.mixins.blocks;

import de.timmi6790.mpmod.McMod;
import de.timmi6790.mpmod.modules.community.CommunityModule;
import net.minecraft.block.BlockBarrier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockBarrier.class)
public class MixinBlockBarrier {

    /**
     * @author
     */
    @Overwrite
    public int getRenderType() {
        if (McMod.getModuleOrThrow(CommunityModule.class).getCache().isShowBarrier()) {
            return 3;
        }
        return -1;
    }
}
