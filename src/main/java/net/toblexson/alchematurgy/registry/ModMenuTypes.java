package net.toblexson.alchematurgy.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalCrucibleMenu;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalSeparatorMenu;

import java.util.function.Supplier;

public class ModMenuTypes
{
    /**
     * The Deferred Register for all the menu types.
     */
    public static DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, Alchematurgy.MOD_ID);

    public static final Supplier<MenuType<AlchemicalCrucibleMenu>> ALCHEMICAL_CRUCIBLE =
        registerMenuType("alchemical_crucible", AlchemicalCrucibleMenu::new);

    public static final Supplier<MenuType<AlchemicalSeparatorMenu>> ALCHEMICAL_SEPARATOR =
            registerMenuType("alchemical_separator", AlchemicalSeparatorMenu::new);


    /**
     * Helper method to register a menu type.
     * @param name The registration name.
     * @param factory The container factory.
     * @return The registered menu type.
     * @param <T> A deferred holder of the menu type.
     */
    private static <T extends AbstractContainerMenu>DeferredHolder<MenuType<?>,MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory)
    {
        return REGISTER.register(name, () -> IMenuTypeExtension.create(factory));
    }

    /**
     * Register all the menu types in the deferred register.
     * @param bus The Event Bus.
     */
    public static void register(IEventBus bus)
    {
        REGISTER.register(bus);
    }
}
