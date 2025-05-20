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

    /**
     * get the essence of the bottled item.
     * @return The item's essence.
     */
    public Essence getEssence()
    {
        return essence;
    }

    /**
     * Get the quality of the essence.
     * @return The essence quality.
     */
    public Essence.Quality getQuality()
    {
        return quality;
    }

    /**
     * Get the usable amount of essence in the bottle.
     * @return The quality essence amount.
     */
    public int getEssenceAmount()
    {
        return quality.getEssenceAmount();
    }
}
