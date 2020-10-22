package de.timmi6790.mpmod.tabsupport;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import de.timmi6790.mpmod.McMod;
import de.timmi6790.mpmod.events.PacketReceiveEvent;
import de.timmi6790.mpmod.events.PacketSendEvent;
import de.timmi6790.mpmod.events.TabCompletionPreEvent;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabSupportManager {
    @Getter
    private final SetMultimap<String, AbstractTabSupport> tabSupports = MultimapBuilder
            .hashKeys()
            .hashSetValues()
            .build();
    private final List<AbstractTabSupport> allTabSupportListeners = new ArrayList<>();

    private String lastTabCompleteRequest;

    public TabSupportManager() {
        McMod.registerEvents(this);
    }

    private void setReturnValue(final TabSupportData tabSupportData) {
        for (final AbstractTabSupport tabSupport : this.allTabSupportListeners) {
            tabSupport.onTabSupport(tabSupportData);
        }

        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
            final GuiChat guichat = (GuiChat) Minecraft.getMinecraft().currentScreen;
            guichat.waitingOnAutocomplete = true;
            guichat.onAutocompleteResponse(tabSupportData.getTabReturn().toArray(new String[0]));
        }
    }

    public void registerTabSupports(final AbstractTabSupport... tabSupports) {
        for (final AbstractTabSupport tabSupport : tabSupports) {
            final List<String> commands = tabSupport.getCommands();
            // If no commands listen to all
            if (commands.isEmpty()) {
                this.allTabSupportListeners.add(tabSupport);
                continue;
            }

            for (final String command : tabSupport.getCommands()) {
                this.tabSupports.put(command, tabSupport);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTabCompleteSend(final PacketSendEvent.Pre event) {
        if (!(event.getPacket() instanceof C14PacketTabComplete) || event.isCanceled()) {
            return;
        }

        final TabSupportData tabSupportData = new TabSupportData(((C14PacketTabComplete) event.getPacket()).getMessage(), new ArrayList<>());
        final TabCompletionPreEvent tabCompletionPreEvent = new TabCompletionPreEvent(tabSupportData);
        try {
            MinecraftForge.EVENT_BUS.post(tabCompletionPreEvent);
        } catch (final Exception ignore) {
        }

        if (tabCompletionPreEvent.isCanceled()) {
            event.setCanceled(true);
            tabSupportData.setTabReturn(tabCompletionPreEvent.getTabSupportData().getTabReturn());
            this.setReturnValue(tabSupportData);
            return;
        }

        this.lastTabCompleteRequest = ((C14PacketTabComplete) event.getPacket()).getMessage();
    }

    @SubscribeEvent
    public void cancelServerRequest(final TabCompletionPreEvent event) {
        for (final AbstractTabSupport tabSupport : this.tabSupports.get(event.getCommandName())) {
            tabSupport.onTabSupport(event.getTabSupportData());
            if (tabSupport.isCancelServerRequest()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTabCompleteReceive(final PacketReceiveEvent.Pre event) {
        if (!(event.getPacket() instanceof S3APacketTabComplete)) {
            return;
        }

        final TabSupportData tabSupportData = new TabSupportData(this.lastTabCompleteRequest, Arrays.asList(((S3APacketTabComplete) event.getPacket()).func_149630_c()));
        for (final AbstractTabSupport tabSupport : this.tabSupports.get(tabSupportData.getMessageArgs()[0])) {
            tabSupport.onTabSupport(tabSupportData);
        }

        event.setCanceled(true);
        this.setReturnValue(tabSupportData);
    }
}
