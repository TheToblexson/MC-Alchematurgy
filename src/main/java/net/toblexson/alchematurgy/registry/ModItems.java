package net.toblexson.alchematurgy.registry;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.Essence;
import net.toblexson.alchematurgy.world.item.BottledEssenceItem;
import net.toblexson.alchematurgy.world.item.BottledMixedEssenceItem;

import java.util.function.Supplier;

/**
 * Alchematurgy's Item registration.
 */
public class ModItems
{
    /**
     * The Deferred Register for all the mod items.
     */
    public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(Alchematurgy.MOD_ID);

    public static final DeferredItem<Item> WAND = item("wand");

    public static final DeferredItem<Item> ASH = item("ash");

    public static final DeferredItem<Item> ALCHEMICAL_ASH = item("alchemical_ash");

    public static final DeferredItem<Item> BOTTLED_MIXED_ESSENCE = item("bottled_mixed_essence", BottledMixedEssenceItem::new);

    public static final DeferredItem<Item> BOTTLED_DIRTY_AIR_ESSENCE = bottledEssence("bottled_dirty_air_essence", Essence.Air, true);
    public static final DeferredItem<Item> BOTTLED_DIRTY_EARTH_ESSENCE = bottledEssence("bottled_dirty_earth_essence", Essence.Earth, true);
    public static final DeferredItem<Item> BOTTLED_DIRTY_FIRE_ESSENCE = bottledEssence("bottled_dirty_fire_essence", Essence.Fire, true);
    public static final DeferredItem<Item> BOTTLED_DIRTY_WATER_ESSENCE = bottledEssence("bottled_dirty_water_essence", Essence.Water, true);
    public static final DeferredItem<Item> BOTTLED_DIRTY_LIFE_ESSENCE = bottledEssence("bottled_dirty_life_essence", Essence.Life, true);
    public static final DeferredItem<Item> BOTTLED_DIRTY_MAGIC_ESSENCE = bottledEssence("bottled_dirty_magic_essence", Essence.Magic, true);

    public static final DeferredItem<Item> BOTTLED_AIR_ESSENCE = bottledEssence("bottled_air_essence", Essence.Air, false);
    public static final DeferredItem<Item> BOTTLED_EARTH_ESSENCE = bottledEssence("bottled_earth_essence", Essence.Earth, false);
    public static final DeferredItem<Item> BOTTLED_FIRE_ESSENCE = bottledEssence("bottled_fire_essence", Essence.Fire, false);
    public static final DeferredItem<Item> BOTTLED_WATER_ESSENCE = bottledEssence("bottled_water_essence", Essence.Water, false);
    public static final DeferredItem<Item> BOTTLED_LIFE_ESSENCE = bottledEssence("bottled_life_essence", Essence.Life, false);
    public static final DeferredItem<Item> BOTTLED_MAGIC_ESSENCE = bottledEssence("bottled_magic_essence", Essence.Magic, false);

    private static DeferredItem<Item> bottledEssence(String name, Essence essence, boolean isDirty)
    {
        return item(name, () -> new BottledEssenceItem(essence, isDirty));
    }

    private static DeferredItem<Item> item(String name, Supplier<Item> itemSupplier)
    {
        return REGISTER.register(name, itemSupplier);
    }

    private static DeferredItem<Item> item(String name)
    {
        return REGISTER.register(name, () -> new Item(new Item.Properties()));
    }

    /**
     * Register all the items in the deferred register.
     * @param bus The Event Bus
     */
    public static void register(IEventBus bus)
    {
        REGISTER.register(bus);
    }
}
