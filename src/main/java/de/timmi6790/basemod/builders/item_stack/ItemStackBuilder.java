package de.timmi6790.basemod.builders.item_stack;

import de.timmi6790.basemod.builders.item_stack.subbuilders.BookItemStackBuilder;
import de.timmi6790.basemod.builders.item_stack.subbuilders.FireworkItemStackBuilder;
import de.timmi6790.basemod.builders.item_stack.subbuilders.PotionItemStackBuilder;
import de.timmi6790.basemod.builders.item_stack.subbuilders.SpawnerItemStackBuilder;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;

public class ItemStackBuilder extends AbstractItemStackBuilder<ItemStackBuilder> {
    protected ItemStackBuilder(final Item item) {
        super(item);
    }

    @Override
    protected ItemStackBuilder getThis() {
        return this;
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
}