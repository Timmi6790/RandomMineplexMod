package de.timmi6790.mpmod.listeners;

import de.timmi6790.mpmod.McMod;
import de.timmi6790.mpmod.utilities.EventUtilities;
import de.timmi6790.mpmod.utilities.MessageBuilder;
import de.timmi6790.mpmod.utilities.VersionUtilities;
import lombok.AllArgsConstructor;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

@AllArgsConstructor
public class UpdateChecker {
    private final McMod mod;

    @SubscribeEvent
    public void onServerJoin(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        // Only run this once
        EventUtilities.unRegisterEvents(this);

        // Only check if we have both a version url and also a download url
        if (this.mod.getDownloadUrl().isEmpty() || this.mod.getVersionUrl().isEmpty()) {
            return;
        }

        Executors.newSingleThreadExecutor().submit(() -> {
                    try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                            new URL(this.mod.getVersionUrl()).openConnection().getInputStream(),
                            StandardCharsets.UTF_8
                    ))) {
                        final String version = bufferedReader.readLine();

                        if (VersionUtilities.hasNewVersion(this.mod.getVersion(), version)) {
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
