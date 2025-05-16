package net.toblexson.alchematurgy.world.item;

import net.minecraft.world.item.Item;
import net.toblexson.alchematurgy.Essence;

public class BottledEssenceItem extends Item
{
    private final Essence essence;
    private final boolean isDirty;

    public BottledEssenceItem(Essence essence, boolean isDirty)
    {
        super(new Item.Properties());
        this.essence = essence;
        this.isDirty = isDirty;
    }

    public Essence getEssence()
    {
        return this.essence;
    }

    public boolean isDirty()
    {
        return isDirty;
    }
}
