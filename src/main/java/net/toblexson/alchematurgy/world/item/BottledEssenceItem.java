package net.toblexson.alchematurgy.world.item;

import net.minecraft.world.item.Item;
import net.toblexson.alchematurgy.Essence;

public class BottledEssenceItem extends Item
{
    private final Essence essence;
    private final Essence.Quality quality;

    public BottledEssenceItem(Essence essence, Essence.Quality quality)
    {
        super(new Item.Properties());
        this.essence = essence;
        this.quality = quality;
    }

    public Essence getEssence()
    {
        return this.essence;
    }

    public Essence.Quality getQuality()
    {
        return quality;
    }
}
