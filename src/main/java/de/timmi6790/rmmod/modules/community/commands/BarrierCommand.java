package de.timmi6790.rmmod.modules.community.commands;

import de.timmi6790.basemod.command.AbstractCommand;
import de.timmi6790.basemod.utilities.MessageBuilder;
import de.timmi6790.rmmod.modules.community.CommunityCache;
import de.timmi6790.rmmod.modules.community.CommunityModule;
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

        communityCache.setShowBarrier(!communityCache.isShowBarrier());
        if (communityCache.isShowBarrier()) {
            this.tell(new MessageBuilder("Barrier blocks are now visible.", EnumChatFormatting.GRAY));
        } else {
            this.tell(new MessageBuilder("We are back to normal.", EnumChatFormatting.GRAY));
        }

        Minecraft.getMinecraft().renderGlobal.loadRenderers();
    }
}
