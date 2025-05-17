package net.toblexson.alchematurgy;

import net.minecraft.world.item.Item;
import net.toblexson.alchematurgy.registry.ModItems;
import org.jetbrains.annotations.Nullable;

public enum Essence
{
    Null("null", -1),
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
        Dirty,
        Pure,
        Concentrated
    }
}
