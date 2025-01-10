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
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Alchematurgy.MOD_ID);

    public static final DeferredItem<Item> WAND = ITEMS.register("wand", () ->
            new Item(new Item.Properties()));
    public static final DeferredItem<Item> ASH = ITEMS.register("ash", () ->
            new Item(new Item.Properties()));

    /**
     * Register all the items in the deferred register.
     * @param bus The Event Bus
     */
    public static void register(IEventBus bus)
    {
        ITEMS.register(bus);
    }
}
