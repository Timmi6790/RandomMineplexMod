package de.timmi6790.mpmod.modules.community.commands;

import de.timmi6790.mpmod.command.AbstractCommand;
import de.timmi6790.mpmod.modules.community.CommunityCache;
import de.timmi6790.mpmod.modules.community.CommunityModule;
import de.timmi6790.mpmod.utilities.MessageBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class BarrierCommand extends AbstractCommand {
    private final CommunityModule communityModule;

    public BarrierCommand(final CommunityModule communityModule) {
        super("barrier");
        this.setPrefix("Debug");

        this.communityModule = communityModule;
    }

    @Override
    public void onCommand(final ICommandSender sender, final String[] args) {
        final CommunityCache communityCache = this.communityModule.getCache();

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
