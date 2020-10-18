package de.timmi6790.mcmod.modules.community.commands;

import de.timmi6790.mcmod.McMod;
import de.timmi6790.mcmod.command.AbstractCommand;
import de.timmi6790.mcmod.modules.community.CommunityCache;
import de.timmi6790.mcmod.modules.community.CommunityModule;
import de.timmi6790.mcmod.utilities.MessageBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class BarrierCommand extends AbstractCommand {
    public BarrierCommand() {
        super("barrier");
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        final CommunityCache communityCache = McMod.getModuleOrThrow(CommunityModule.class).getCache();

        if (communityCache.isShowBarrier()) {
            communityCache.setShowBarrier(false);
            this.tell(new MessageBuilder("We are back to normal.", EnumChatFormatting.GRAY));

        } else {
            communityCache.setShowBarrier(true);
            this.tell(new MessageBuilder("Barrier blocks are now visible.", EnumChatFormatting.GRAY));
        }

        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }
}
