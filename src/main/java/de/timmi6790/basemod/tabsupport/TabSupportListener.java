package de.timmi6790.basemod.tabsupport;

import de.timmi6790.basemod.events.PacketReceiveEvent;
import de.timmi6790.basemod.events.PacketSendEvent;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

@RequiredArgsConstructor
public class TabSupportListener {
    private final TabSupportManager tabSupportManager;

    private String lastTabCompleteRequest;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTabCompleteSend(final PacketSendEvent.Pre event) {
        if (event.getPacket() instanceof C14PacketTabComplete && !event.isCanceled()) {
            this.lastTabCompleteRequest = ((C14PacketTabComplete) event.getPacket()).getMessage();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onTabCompleteReceive(final PacketReceiveEvent.Pre event) {
        if (!(event.getPacket() instanceof S3APacketTabComplete) || event.isCanceled()) {
            return;
        }

        final S3APacketTabComplete tabComplete = (S3APacketTabComplete) event.getPacket();
        final TabSupportData tabSupportData = new TabSupportData(
                this.lastTabCompleteRequest,
                Arrays.asList(((S3APacketTabComplete) event.getPacket()).func_149630_c())
        );

        // Get the command specific data
        for (final AbstractTabSupport tabSupport : this.tabSupportManager.getTabSupports(tabSupportData.getCommand())) {
            tabSupport.onTabSupport(tabSupportData);
        }

        // Parse the all command data
        for (final AbstractTabSupport tabSupport : this.tabSupportManager.getAllTabSupportListeners()) {
            tabSupport.onTabSupport(tabSupportData);
        }

        tabComplete.matches = tabSupportData.getTabReturn().toArray(new String[0]);
    }
}
