package de.timmi6790.basemod.builders.item_stack.subbuilders;

import de.timmi6790.basemod.builders.item_stack.AbstractItemStackBuilder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.Collection;

@Getter
@Setter
public class BookItemStackBuilder extends AbstractItemStackBuilder<BookItemStackBuilder> {
    private Integer generation = 0;
    private Boolean resolved = true;
    private String author = "The Void";
    private String title = "";
    private String[] pages = new String[0];

    public BookItemStackBuilder() {
        super(Items.written_book);
    }

    @Override
    protected BookItemStackBuilder getThis() {
        return this;
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

    public BookItemStackBuilder setPages(final Collection<String> pages) {
        this.setPages(pages.toArray(new String[0]));
        return this;
    }

    public BookItemStackBuilder setPages(final String... pages) {
        this.pages = pages.clone();
        return this;
    }

    public String[] getPages() {
        return this.pages.clone();
    }
}