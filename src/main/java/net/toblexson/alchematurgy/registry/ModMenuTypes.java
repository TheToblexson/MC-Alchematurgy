package net.toblexson.alchematurgy.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalCrucibleMenu;

import java.util.function.Supplier;

public class ModMenuTypes
{
    /**
     * The Deferred Register for all the menu types.
     */
    public static DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, Alchematurgy.MOD_ID);

    public static final Supplier<MenuType<AlchemicalCrucibleMenu>> ALCHEMICAL_CRUCIBLE_MENU = REGISTER.register("alchemical_crucible_menu", () ->
            new MenuType<>(AlchemicalCrucibleMenu::new, FeatureFlags.DEFAULT_FLAGS));

    /**
     * Register all the menu types in the deferred register.
     * @param bus The Event Bus.
     */
    public static void register(IEventBus bus)
    {
        REGISTER.register(bus);
    }
}
