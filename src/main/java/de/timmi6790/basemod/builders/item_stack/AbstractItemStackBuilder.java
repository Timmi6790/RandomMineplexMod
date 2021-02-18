package de.timmi6790.basemod.builders.item_stack;

import de.timmi6790.basemod.builders.item_stack.modifiers.AttributeOperation;
import de.timmi6790.basemod.builders.item_stack.modifiers.AttributeType;
import lombok.Getter;
import lombok.NonNull;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

import java.util.*;

@Getter
public abstract class AbstractItemStackBuilder<SELF extends AbstractItemStackBuilder<SELF>> {
    private Item item;
    private int itemCount = 1;

    // Attributes
    private final List<AttributeModifier> attributes = new ArrayList<>();

    // Enchantments
    private final Map<Integer, Integer> enchantments = new HashMap<>();

    // Display
    private final List<String> itemLore = new ArrayList<>();
    private String itemName;

    // Itemstack
    private Integer itemDamage;
    private Boolean unbreakable;
    private boolean hideFlags = false;

    protected AbstractItemStackBuilder() {
        this(Items.stick);
    }

    protected AbstractItemStackBuilder(final Item item) {
        this.item = item;
    }

    protected abstract SELF getThis();

    public ItemStack build() {
        return this.build(false);
    }

    public ItemStack build(final boolean requireNbt) {
        final ItemStack itemStack = new ItemStack(this.item, this.itemCount);
        final NBTTagCompound nbtTag = new NBTTagCompound();

        // Display
        final NBTTagCompound display = new NBTTagCompound();
        if (this.itemName != null) {
            display.setString("Name", this.itemName);

        }

        if (!this.itemLore.isEmpty()) {
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

        // Attributes
        if (!this.attributes.isEmpty()) {
            final NBTTagList attributes = new NBTTagList();
            for (final AttributeModifier modifier : this.attributes) {
                final NBTTagCompound attribute = new NBTTagCompound();
                attribute.setTag("AttributeName", new NBTTagString(modifier.getName()));
                attribute.setTag("Name", new NBTTagString(modifier.getName()));
                attribute.setTag("Amount", new NBTTagDouble(modifier.getAmount()));
                attribute.setTag("Operation", new NBTTagInt(modifier.getOperation()));
                attribute.setTag("UUIDLeast", new NBTTagLong(modifier.getID().getLeastSignificantBits()));
                attribute.setTag("UUIDMost", new NBTTagLong(modifier.getID().getMostSignificantBits()));

                attributes.appendTag(attribute);
            }
            nbtTag.setTag("AttributeModifiers", attributes);
        }

        // Unbreakable
        if (this.unbreakable != null) {
            nbtTag.setBoolean("Unbreakable", this.unbreakable);
        }

        if (this.itemDamage != null) {
            itemStack.setItemDamage(this.itemDamage);
        }

        if (this.hideFlags) {
            nbtTag.setInteger("HideFlags", 1);
        }

        if (!nbtTag.getKeySet().isEmpty() || requireNbt) {
            itemStack.setTagCompound(nbtTag);
        }

        return itemStack;
    }

    public SELF setItemLore(final String... itemLore) {
        this.itemLore.clear();
        this.itemLore.addAll(Arrays.asList(itemLore));
        return this.getThis();
    }

    public SELF addItemLore(final String... itemLore) {
        this.itemLore.addAll(Arrays.asList(itemLore));
        return this.getThis();
    }

    public SELF addAttribute(final AttributeType attributeType,
                             final AttributeOperation attributeOperation,
                             final double value) {
        return this.addAttribute(new AttributeModifier(
                attributeType.getId(),
                value,
                attributeOperation.getId()
        ));
    }

    public SELF addAttribute(final AttributeModifier attributeModifier) {
        this.attributes.add(attributeModifier);
        return this.getThis();
    }

    public SELF addEnchantment(final Enchantment enchantment, final Integer level) {
        return this.addEnchantment(enchantment.effectId, level);
    }

    public SELF addEnchantment(final Integer id, final Integer level) {
        this.enchantments.put(id, level);
        return this.getThis();
    }

    public SELF setItemCount(final int itemCount) {
        this.itemCount = itemCount;
        return this.getThis();
    }

    public SELF setItemName(final String itemName) {
        this.itemName = itemName;
        return this.getThis();
    }

    public SELF setItemDamage(final Integer itemDamage) {
        this.itemDamage = itemDamage;
        return this.getThis();
    }

    public SELF setUnbreakable(final Boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this.getThis();
    }

    public SELF setItem(@NonNull final Item item) {
        this.item = item;
        return this.getThis();
    }

    public SELF setHideFlags(final boolean hideFlags) {
        this.hideFlags = hideFlags;
        return this.getThis();
    }
}
