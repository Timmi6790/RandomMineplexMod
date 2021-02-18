package de.timmi6790.basemod.builders.item_stack.subbuilders;

import de.timmi6790.basemod.builders.item_stack.AbstractItemStackBuilder;
import lombok.Data;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;

import java.util.ArrayList;
import java.util.List;

public class PotionItemStackBuilder extends AbstractItemStackBuilder<PotionItemStackBuilder> {
    private final List<PotionEffectData> potionEffectDataList = new ArrayList<>();

    public PotionItemStackBuilder() {
        super(Items.potionitem);
    }

    @Override
    protected PotionItemStackBuilder getThis() {
        return this;
    }

    @Override
    public ItemStack build() {
        final ItemStack itemStack = super.build(true);
        final NBTTagCompound nbtTag = itemStack.getTagCompound();

        // Potions
        final NBTTagList effects = new NBTTagList();
        for (final PotionEffectData potionEffectData : this.potionEffectDataList) {
            final NBTTagCompound effect = new NBTTagCompound();
            effect.setInteger("Id", potionEffectData.getPotionId());
            effect.setInteger("Amplifier", potionEffectData.getAmplifier());
            effect.setInteger("Duration", potionEffectData.getDuration());
            effects.appendTag(effect);
        }

        itemStack.setTagInfo("CustomPotionEffects", effects);

        itemStack.setTagCompound(nbtTag);
        return itemStack;
    }

    public PotionItemStackBuilder addPotionEffect(final Potion potionEffect, final int amplifier, final int duration) {
        return this.addPotionEffect(potionEffect.getId(), amplifier, duration);
    }

    public PotionItemStackBuilder addPotionEffect(final int potionId, final int amplifier, final int duration) {
        this.potionEffectDataList.add(
                new PotionEffectData(
                        potionId,
                        amplifier,
                        duration
                )
        );
        return this;
    }

    @Data
    private static class PotionEffectData {
        private final int potionId;
        private final int amplifier;
        private final int duration;
    }
}
