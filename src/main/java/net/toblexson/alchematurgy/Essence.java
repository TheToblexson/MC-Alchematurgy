package net.toblexson.alchematurgy;

import net.minecraft.world.item.Item;
import net.toblexson.alchematurgy.registry.ModItems;
import org.jetbrains.annotations.Nullable;

public enum Essence
{
    Air("air", 0),
    Earth("earth", 1),
    Fire("fire", 2),
    Water("life", 3),
    Life("life", 4),
    Magic("magic", 5);

    public final String name;
    public final int index;

    Essence(String name, int index)
    {
        this.name = name;
        this.index = index;
    }

    /**
     * Convert the index to a bottled pure essence item.
     * @param index The essence index.
     * @return The bottled pure essence corresponding to the index.
     */
    @Nullable
    public static Item getPureEssence(int index)
    {
        return switch (index)
        {
            case 0 -> ModItems.BOTTLED_AIR_ESSENCE.get();
            case 1 -> ModItems.BOTTLED_EARTH_ESSENCE.get();
            case 2 -> ModItems.BOTTLED_FIRE_ESSENCE.get();
            case 3 -> ModItems.BOTTLED_WATER_ESSENCE.get();
            case 4 -> ModItems.BOTTLED_LIFE_ESSENCE.get();
            case 5 -> ModItems.BOTTLED_MAGIC_ESSENCE.get();
            default -> null;
        };
    }

    /**
     * Convert the index to a bottled concentrated essence item.
     * @param index The essence index.
     * @return The bottled concentrated essence corresponding to the index.
     */
    @Nullable
    public static Item getConcentratedEssence(int index)
    {
        return switch (index)
        {
            case 0 -> ModItems.BOTTLED_CONCENTRATED_AIR_ESSENCE.get();
            case 1 -> ModItems.BOTTLED_CONCENTRATED_EARTH_ESSENCE.get();
            case 2 -> ModItems.BOTTLED_CONCENTRATED_FIRE_ESSENCE.get();
            case 3 -> ModItems.BOTTLED_CONCENTRATED_WATER_ESSENCE.get();
            case 4 -> ModItems.BOTTLED_CONCENTRATED_LIFE_ESSENCE.get();
            case 5 -> ModItems.BOTTLED_CONCENTRATED_MAGIC_ESSENCE.get();
            default -> null;
        };
    }
    public enum Quality
    {
        Dirty(1),
        Pure(2),
        Concentrated(4);

        private final int essenceAmount;

        Quality(int essenceAmount)
        {
            this.essenceAmount = essenceAmount;
        }

        /**
         * Get the usable amount of essence that the quality gives.
         * @return The essence amount.
         */
        public int getEssenceAmount()
        {
            return essenceAmount;
        }
    }
}
