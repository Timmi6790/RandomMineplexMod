package de.timmi6790.basemod.builders.item_stack.subbuilders;

import de.timmi6790.basemod.builders.item_stack.AbstractItemStackBuilder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

@Getter
@Setter
public class SpawnerItemStackBuilder extends AbstractItemStackBuilder<SpawnerItemStackBuilder> {
    private final String entityName;
    private Short spawnDelay;
    private Short minSpawnDelay;
    private Short maxSpawnDelay;
    private Short spawnCount;
    private Short maxNearbyEntities;
    private Short requiredPlayerRange;
    private Short spawnRange;

    public SpawnerItemStackBuilder(final String entityName) {
        super(Item.getItemFromBlock(Blocks.mob_spawner));

        this.entityName = entityName;
    }

    @Override
    protected SpawnerItemStackBuilder getThis() {
        return this;
    }

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
}