package de.timmi6790.basemod.builders.item_stack.subbuilders;

import de.timmi6790.basemod.builders.item_stack.AbstractItemStackBuilder;
import de.timmi6790.basemod.builders.item_stack.modifiers.FireworkType;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

@Getter
@Setter
public class FireworkItemStackBuilder extends AbstractItemStackBuilder<FireworkItemStackBuilder> {
    private final NBTTagList explosions = new NBTTagList();
    private byte flight = 1;

    public FireworkItemStackBuilder() {
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
    protected FireworkItemStackBuilder getThis() {
        return this;
    }

    @Override
    public ItemStack build() {
        final ItemStack itemStack = super.build(true);

        final NBTTagCompound firework = new NBTTagCompound();
        firework.setByte("Flight", this.flight);
        firework.setTag("Explosions", this.explosions);

        itemStack.getTagCompound().setTag("Fireworks", firework);
        return itemStack;
    }

    public FireworkItemStackBuilder addExplosion(final FireworkType type,
                                                 final EnumDyeColor[] colours,
                                                 final EnumDyeColor[] fadeColours,
                                                 final boolean trail,
                                                 final boolean flicker) {
        final NBTTagCompound effect = new NBTTagCompound();

        effect.setByte("Type", (byte) type.ordinal());
        effect.setIntArray("Colors", this.coloursToIntArray(colours));
        effect.setIntArray("FadeColors", this.coloursToIntArray(fadeColours));
        effect.setBoolean("Trail", trail);
        effect.setBoolean("Flicker", flicker);

        this.explosions.appendTag(effect);

        return this;
    }

}
