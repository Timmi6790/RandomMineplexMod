package de.timmi6790.mpmod.listeners;

import de.timmi6790.mpmod.McMod;
import de.timmi6790.mpmod.utilities.EventUtilities;
import de.timmi6790.mpmod.utilities.MessageBuilder;
import lombok.AllArgsConstructor;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;

@AllArgsConstructor
public class UpdateChecker {
    private final McMod mod;

    private boolean hasNewVersion(final String currentVersion, final String otherVersion) {
        final StringTokenizer currentVersionToken = new StringTokenizer(currentVersion, ".");
        final StringTokenizer otherVersionToken = new StringTokenizer(otherVersion, ".");

        while (currentVersionToken.hasMoreTokens() || otherVersionToken.hasMoreElements()) {
            String currentPart = "0";
            if (currentVersionToken.hasMoreTokens()) {
                currentPart = currentVersionToken.nextToken();
            }

            String otherPart = "0";
            if (otherVersionToken.hasMoreTokens()) {
                otherPart = otherVersionToken.nextToken();
            }

            if (otherPart.compareTo(currentPart) > 0) {
                return true;
            }
        }

        return false;
    }

    @SubscribeEvent
    public void onServerJoin(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        // Only run this once
        EventUtilities.unRegisterEvents(this);

        Executors.newSingleThreadExecutor().submit(() -> {
                    try {
                        final URLConnection connection = new URL(this.mod.getVersionUrl()).openConnection();
                        final String version = new BufferedReader(new InputStreamReader(
                                connection.getInputStream(),
                                StandardCharsets.UTF_8
                        )).readLine();

                        if (!this.mod.getDownloadUrl().isEmpty() && this.hasNewVersion(this.mod.getVersion(), version)) {
                            new MessageBuilder(this.mod.getModName(), EnumChatFormatting.YELLOW)
                                    .addMessage("\n\nA new version is available!", EnumChatFormatting.GRAY)
                                    .addMessage("\nCurrent version: ", EnumChatFormatting.GRAY)
                                    .addMessage(this.mod.getVersion(), EnumChatFormatting.YELLOW)
                                    .addMessage(" New version: ", EnumChatFormatting.GRAY)
                                    .addMessage(version, EnumChatFormatting.YELLOW)
                                    .addMessage(
                                            new MessageBuilder("\nClick me to find the new version", EnumChatFormatting.YELLOW)
                                                    .addClickEvent(ClickEvent.Action.OPEN_URL, this.mod.getDownloadUrl())
                                    )
                                    .addBoxedToMessage()
                                    .sendToPlayerDelayed(45);
                        }
                    } catch (final IOException ignore) {
                    }
                }
        );
    }
}
