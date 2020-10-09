package de.timmi6790.mcmod.datatypes.items;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import de.timmi6790.mcmod.datatypes.Pair;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ItemStackBuilder {
    private final Item item;
    // Enchantments
    private final Map<Integer, Integer> enchantments = new HashMap<>();
    // Display
    private String[] itemLore;
    private String itemName;
    // Itemstack
    private Integer itemDamage;
    private Boolean unbreakable;

    private ItemStackBuilder(final Item item) {
        this.item = item;
    }

    public static FireworkItemStackBuilder ofFirework() {
        return new FireworkItemStackBuilder();
    }

    public static SpawnerItemStackBuilder ofSpawner(final int entityId) {
        return ItemStackBuilder.ofSpawner(EntityList.getStringFromID(entityId));
    }

    public static SpawnerItemStackBuilder ofSpawner(final Entity entity) {
        return ItemStackBuilder.ofSpawner(EntityList.getEntityString(entity));
    }

    public static SpawnerItemStackBuilder ofSpawner(final String entityName) {
        return new SpawnerItemStackBuilder(entityName);
    }

    public static BookItemStackBuilder ofBook() {
        return new BookItemStackBuilder();
    }

    public static PotionItemStackBuilder ofPotion() {
        return new PotionItemStackBuilder();
    }

    public static ItemStackBuilder of(final Block block) {
        return ItemStackBuilder.of(Item.getItemFromBlock(block));
    }

    public static ItemStackBuilder of(final Item item) {
        return new ItemStackBuilder(item);
    }

    public ItemStack build() {
        return this.build(false);
    }

    public ItemStack build(final boolean requireNbt) {
        final ItemStack itemStack = new ItemStack(this.item);
        final NBTTagCompound nbtTag = new NBTTagCompound();

        // Display
        final NBTTagCompound display = new NBTTagCompound();
        if (this.itemName != null) {
            display.setString("Name", this.itemName);

        }

        if (this.itemLore != null && this.itemLore.length != 0) {
            final NBTTagList loreNbt = new NBTTagList();

            for (final String lore : this.itemLore) {
                loreNbt.appendTag(new NBTTagString(lore));
            }

            display.setTag("Lore", loreNbt);
        }

        if (!display.getKeySet().isEmpty()) {
            nbtTag.setTag("display", display);
        }

        // Enchantments
        if (!this.enchantments.isEmpty()) {
            final NBTTagList enchList = new NBTTagList();
            for (final Map.Entry<Integer, Integer> entry : this.enchantments.entrySet()) {
                final NBTTagCompound ench = new NBTTagCompound();
                ench.setInteger("lvl", entry.getValue());
                ench.setInteger("id", entry.getKey());
                enchList.appendTag(ench);
            }
            nbtTag.setTag("ench", enchList);
        }

        // Unbreakable
        if (this.unbreakable != null) {
            nbtTag.setBoolean("Unbreakable", this.unbreakable);
        }

        if (this.itemDamage != null) {
            itemStack.setItemDamage(this.itemDamage);
        }

        if (!nbtTag.getKeySet().isEmpty() || requireNbt) {
            itemStack.setTagCompound(nbtTag);
        }

        return itemStack;
    }

    public ItemStackBuilder setItemLore(final String... itemLore) {
        this.itemLore = itemLore;
        return this;
    }

    public ItemStackBuilder setItemName(final String itemName) {
        this.itemName = itemName;
        return this;
    }


    public ItemStackBuilder addEnchantment(final Enchantment enchantment, final Integer level) {
        return this.addEnchantment(enchantment.effectId, level);
    }

    public ItemStackBuilder addEnchantment(final Integer id, final Integer level) {
        this.enchantments.put(id, level);
        return this;
    }

    public ItemStackBuilder setItemDamage(final int itemDamage) {
        this.itemDamage = itemDamage;
        return this;
    }

    public ItemStackBuilder setUnbreakable(final boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public static class BookItemStackBuilder extends ItemStackBuilder {
        private Integer generation = 0;
        private Boolean resolved = true;
        private String author = "The Void";
        private String title = "";
        private String[] pages = new String[0];

        private BookItemStackBuilder() {
            super(Items.written_book);
        }

        @Override
        public ItemStack build() {
            final ItemStack itemStack = super.build(true);
            final NBTTagCompound nbtTag = itemStack.getTagCompound();

            nbtTag.setInteger("generation", this.generation);
            nbtTag.setBoolean("resolved", this.resolved);
            nbtTag.setString("author", this.author);
            nbtTag.setString("title", this.title);

            final NBTTagList pages = new NBTTagList();
            for (final String page : this.pages) {
                pages.appendTag(new NBTTagString(page));
            }
            nbtTag.setTag("pages", pages);

            itemStack.setTagCompound(nbtTag);
            return itemStack;
        }

        public BookItemStackBuilder setGeneration(final int generation) {
            this.generation = generation;
            return this;
        }

        public BookItemStackBuilder setResolved(final boolean resolved) {
            this.resolved = resolved;
            return this;
        }

        public BookItemStackBuilder setAuthor(final String author) {
            this.author = author;
            return this;
        }

        public BookItemStackBuilder setTitle(final String title) {
            this.title = title;
            return this;
        }

        public BookItemStackBuilder setPages(final Collection<String> pages) {
            this.setPages(pages.toArray(new String[0]));
            return this;
        }

        public BookItemStackBuilder setPages(final String... pages) {
            this.pages = pages;
            return this;
        }
    }

    public static class PotionItemStackBuilder extends ItemStackBuilder {
        @SuppressWarnings("UnstableApiUsage")
        private final Multimap<Integer, Pair<Integer, Integer>> potions = MultimapBuilder
                .hashKeys()
                .arrayListValues()
                .build();

        private PotionItemStackBuilder() {
            super(Items.potionitem);
        }

        @Override
        public ItemStack build() {
            final ItemStack itemStack = super.build(true);
            final NBTTagCompound nbtTag = itemStack.getTagCompound();

            // Potions
            final NBTTagList effects = new NBTTagList();
            for (final Map.Entry<Integer, Pair<Integer, Integer>> entry : this.potions.entries()) {
                final NBTTagCompound effect = new NBTTagCompound();
                effect.setInteger("Id", entry.getKey());
                effect.setInteger("Amplifier", entry.getValue().getLeft());
                effect.setInteger("Duration", entry.getValue().getRight());
                effects.appendTag(effect);
            }

            itemStack.setTagInfo("CustomPotionEffects", effects);

            itemStack.setTagCompound(nbtTag);
            return itemStack;
        }

        public PotionItemStackBuilder addPotionEffect(final Potion potionEffect, final int amplifier, final int duration) {
            this.potions.put(potionEffect.getId(), new Pair<>(amplifier, duration));
            return this;
        }

        public PotionItemStackBuilder addPotionEffect(final int id, final int amplifier, final int duration) {
            this.potions.put(id, new Pair<>(amplifier, duration));
            return this;
        }
    }

    public static class SpawnerItemStackBuilder extends ItemStackBuilder {
        private final String entityName;
        private Short spawnDelay;
        private Short minSpawnDelay;
        private Short maxSpawnDelay;
        private Short spawnCount;
        private Short maxNearbyEntities;
        private Short requiredPlayerRange;
        private Short spawnRange;

        private SpawnerItemStackBuilder(final String entityName) {
            super(Item.getItemFromBlock(Blocks.mob_spawner));

            this.entityName = entityName;
        }


            /*
            if (this.getRandomEntity() != null)
        {
            nbt.setTag("SpawnData", this.getRandomEntity().nbtData.copy());
        }

            if (this.getRandomEntity() != null || this.minecartToSpawn.size() > 0)
        {
            NBTTagList nbttaglist = new NBTTagList();

            if (this.minecartToSpawn.size() > 0)
            {
                for (MobSpawnerBaseLogic.WeightedRandomMinecart mobspawnerbaselogic$weightedrandomminecart : this.minecartToSpawn)
                {
                    nbttaglist.appendTag(mobspawnerbaselogic$weightedrandomminecart.toNBT());
                }
            }
            else
            {
                nbttaglist.appendTag(this.getRandomEntity().toNBT());
            }

            nbt.setTag("SpawnPotentials", nbttaglist);
        }

             */

        @Override
        public ItemStack build() {
            final ItemStack itemStack = super.build(true);
            final NBTTagCompound nbtTag = itemStack.getTagCompound();

            final NBTTagCompound spawnData = new NBTTagCompound();
            spawnData.setString("EntityId", this.entityName);

            if (this.spawnDelay != null) {
                spawnData.setShort("Delay", this.spawnDelay);
                spawnData.setShort("MinSpawnDelay", this.minSpawnDelay == null ? 1 : this.minSpawnDelay);
                spawnData.setShort("MaxSpawnDelay", this.maxSpawnDelay == null ? 1 : this.maxSpawnDelay);
            }

            if (this.spawnCount != null) {
                spawnData.setShort("SpawnCount", this.spawnCount);
            }

            if (this.maxNearbyEntities != null) {
                spawnData.setShort("MaxNearbyEntities", this.maxNearbyEntities);
            }

            if (this.requiredPlayerRange != null) {
                spawnData.setShort("RequiredPlayerRange", this.requiredPlayerRange);
            }

            if (this.spawnRange != null) {
                spawnData.setShort("SpawnRange", this.spawnRange);
            }

            nbtTag.setTag("BlockEntityTag", spawnData);
            itemStack.setTagCompound(nbtTag);
            return itemStack;
        }

        public SpawnerItemStackBuilder setSpawnDelay(final Short spawnDelay) {
            this.spawnDelay = spawnDelay;
            return this;
        }

        public SpawnerItemStackBuilder setMinSpawnDelay(final Short minSpawnDelay) {
            this.minSpawnDelay = minSpawnDelay;
            return this;
        }

        public SpawnerItemStackBuilder setMaxSpawnDelay(final Short maxSpawnDelay) {
            this.maxSpawnDelay = maxSpawnDelay;
            return this;
        }

        public SpawnerItemStackBuilder setSpawnCount(final Short spawnCount) {
            this.spawnCount = spawnCount;
            return this;
        }

        public SpawnerItemStackBuilder setMaxNearbyEntities(final Short maxNearbyEntities) {
            this.maxNearbyEntities = maxNearbyEntities;
            return this;
        }

        public SpawnerItemStackBuilder setRequiredPlayerRange(final Short requiredPlayerRange) {
            this.requiredPlayerRange = requiredPlayerRange;
            return this;
        }

        public SpawnerItemStackBuilder setSpawnRange(final Short spawnRange) {
            this.spawnRange = spawnRange;
            return this;
        }
    }

    public static class FireworkItemStackBuilder extends ItemStackBuilder {
        private final NBTTagList explosions = new NBTTagList();
        private byte flight = 1;

        private FireworkItemStackBuilder() {
            super(Items.fireworks);
        }

        private int[] coloursToIntArray(final EnumDyeColor[] colours) {
            final int[] coloursInt = new int[colours.length];
            for (int index = 0; colours.length > index; index++) {
                coloursInt[index] = ItemDye.dyeColors[colours[index].getDyeDamage()];
            }
            return coloursInt;
        }

        @Override
        public ItemStack build() {
            final ItemStack itemStack = this.build(true);

            final NBTTagCompound firework = new NBTTagCompound();
            firework.setByte("Flight", this.flight);
            firework.setTag("Explosions", this.explosions);

            itemStack.getTagCompound().setTag("Fireworks", firework);
            return itemStack;
        }

        public FireworkItemStackBuilder setFlight(final byte flight) {
            this.flight = flight;
            return this;
        }

        public FireworkItemStackBuilder addExplosion(final FireworkType type, final EnumDyeColor[] colours, final EnumDyeColor[] fadeColours, final boolean trail, final boolean flicker) {
            final NBTTagCompound effect = new NBTTagCompound();

            effect.setByte("Type", (byte) type.ordinal());
            effect.setIntArray("Colors", this.coloursToIntArray(colours));
            effect.setIntArray("FadeColors", this.coloursToIntArray(fadeColours));
            effect.setBoolean("Trail", trail);
            effect.setBoolean("Flicker", flicker);

            this.explosions.appendTag(effect);

            return this;
        }

        public enum FireworkType {
            SMALL_BALL,
            LARGE_BALL,
            STAR_SHAPED,
            CREEPER_SHAPED,
            BURST
        }
    }
}