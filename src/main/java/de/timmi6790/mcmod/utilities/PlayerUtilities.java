package de.timmi6790.mcmod.utilities;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@UtilityClass
public class PlayerUtilities {
    public final List<Block> BLOCK_BLACKLIST = Collections.unmodifiableList(Arrays.asList(Blocks.enchanting_table, Blocks.chest, Blocks.ender_chest, Blocks.trapped_chest,
            Blocks.anvil, Blocks.sand, Blocks.web, Blocks.torch, Blocks.crafting_table, Blocks.furnace, Blocks.waterlily, Blocks.dispenser,
            Blocks.stone_pressure_plate, Blocks.wooden_pressure_plate, Blocks.noteblock, Blocks.dropper, Blocks.tnt, Blocks.standing_banner, Blocks.wall_banner,
            Blocks.brown_mushroom, Blocks.red_mushroom, Blocks.stone_slab, Blocks.stone_slab2, Blocks.wooden_slab));
    private final Cache<UUID, String> playerNameCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(20, TimeUnit.MINUTES)
            .build();

    public Optional<String> uuidToName(final UUID uuid) {
        final String playerName = playerNameCache.getIfPresent(uuid);
        if (playerName != null) {
            return Optional.of(playerName);
        }

        try {
            CompletableFuture.runAsync(() -> {
                try {
                    final URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + PlayerUtilities.uuidShorter(uuid));
                    final URLConnection con = url.openConnection();

                    final JsonElement element = new JsonParser().parse(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                    final JsonObject jsonObject = element.getAsJsonObject();

                    final String name = jsonObject.get("name").getAsString();
                    playerNameCache.put(uuid, name);

                } catch (final Exception ignore) {
                }
            }).get(20, TimeUnit.SECONDS);
        } catch (final InterruptedException | ExecutionException | TimeoutException e) {
            return Optional.empty();
        }


        return Optional.ofNullable(playerNameCache.getIfPresent(uuid));
    }

    private String uuidShorter(final UUID uuid) {
        return uuid.toString().replace("-", "");
    }

    public Set<String> getAllPlayersInLobby() {
        final Set<String> playersInLobby = new HashSet<>();
        for (final NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            playersInLobby.add(playerInfo.getGameProfile().getName());
        }

        return playersInLobby;
    }

    public Optional<BlockPos> getPlayerBlockPos() {
        if (!Shortcuts.getPlayer().isPresent()) {
            return Optional.empty();
        }

        return Optional.of(getPlayerBlockPos(Shortcuts.getPlayer().get().getPositionVector()));
    }

    public BlockPos getPlayerBlockPos(final Vec3 playerPositionVector) {
        final int blockX = playerPositionVector.xCoord >= 0 ? (int) playerPositionVector.xCoord : (int) playerPositionVector.xCoord - 1;
        final int blockZ = playerPositionVector.zCoord >= 0 ? (int) playerPositionVector.zCoord : (int) playerPositionVector.zCoord - 1;
        return new BlockPos(blockX, playerPositionVector.yCoord, blockZ);
    }

    public boolean hasPlayerMovedLastTick() {
        if (!Shortcuts.getPlayer().isPresent()) {
            return false;
        }

        final EntityPlayerSP player = Shortcuts.getPlayer().get();
        return player.posX != player.prevPosX || player.posY != player.prevPosY || player.posZ != player.prevPosZ;
    }

    public ItemStack[] getHotbar() {
        return Arrays.copyOfRange(Shortcuts.getPlayer().get().inventory.mainInventory, 0, 9);
    }

    public int getCurrentSlotId() {
        return Minecraft.getMinecraft().thePlayer.inventory.currentItem;
    }

    public void setCurrentSlotId(final int id) {
        if (id == PlayerUtilities.getCurrentSlotId()) {
            return;
        }

        Shortcuts.getPlayer().get().inventory.currentItem = id;
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C09PacketHeldItemChange(id));
    }

    public Optional<Integer> getBlockSlotInHotbar() {
        if (!Shortcuts.getPlayer().isPresent()) {
            return Optional.empty();
        }

        final ItemStack heldItem = Shortcuts.getPlayer().get().getHeldItem();
        if (heldItem != null && heldItem.getItem() instanceof ItemBlock && !BLOCK_BLACKLIST.contains(((ItemBlock) heldItem.getItem()).getBlock())) {
            return Optional.of(Shortcuts.getPlayer().get().inventory.currentItem);
        }

        final ItemStack[] hotbar = getHotbar();
        for (int index = 0; hotbar.length > index; index++) {
            if (hotbar[index] != null && hotbar[index].getItem() instanceof ItemBlock && !BLOCK_BLACKLIST.contains(((ItemBlock) hotbar[index].getItem()).getBlock())) {
                return Optional.of(index);
            }
        }

        return Optional.empty();
    }

    public Optional<EntityPlayer> getClosetPlayer() {
        if (!Shortcuts.getPlayer().isPresent() || !Shortcuts.getWorld().isPresent()) {
            return Optional.empty();
        }

        final EntityPlayer player = Shortcuts.getPlayer().get();

        EntityPlayer closetPlayer = null;
        double closetPlayerDistance = -1;
        for (final EntityPlayer entity : Shortcuts.getWorld().get().playerEntities) {
            if (entity == player) {
                continue;
            }

            final double distance = player.getPositionVector().distanceTo(entity.getPositionVector());
            if (closetPlayerDistance == -1 || closetPlayerDistance > distance) {
                closetPlayerDistance = distance;
                closetPlayer = entity;
            }
        }

        return Optional.ofNullable(closetPlayer);
    }

    public boolean isCurrentItemSword() {
        if (!Shortcuts.getPlayer().isPresent()) {
            return false;
        }

        return Shortcuts.getPlayer().get().getHeldItem() != null && Shortcuts.getPlayer().get().getHeldItem().getItem() instanceof ItemSword;
    }
}
