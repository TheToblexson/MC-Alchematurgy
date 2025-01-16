package net.toblexson.alchematurgy.registry;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;

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

    public static final DeferredItem<Item> EARTH = item("element_earth");
    public static final DeferredItem<Item> WATER = item("element_water");
    public static final DeferredItem<Item> AIR = item("element_air");
    public static final DeferredItem<Item> FIRE = item("element_fire");

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
