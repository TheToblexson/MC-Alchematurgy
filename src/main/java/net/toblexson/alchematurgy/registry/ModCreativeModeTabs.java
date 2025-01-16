package net.toblexson.alchematurgy.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;

import java.util.function.Supplier;

/**
 * Alchematurgy's Creative Tab registration.
 */
public class ModCreativeModeTabs
{
    /** The Deferred Register for Alchematurgy's creative mode tabs.
     */
    public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Alchematurgy.MOD_ID);

    public static Supplier<CreativeModeTab> ALCHEMATURGY_TAB = REGISTER.register("alchematurgy_tab", ()->
            CreativeModeTab.builder()
                    .icon(()-> new ItemStack(ModItems.WAND.get()))
                    .title(Component.translatable("creativetab.alchematurgy.alchematurgy_tab"))
                    .displayItems((itemsDisplayParameters, output) ->
                    {
                        // Adds all items and block items to the tab
                        ModItems.REGISTER.getEntries().stream().map(Holder::value).forEach(output::accept);
                    }).build());

    /** Register all the creative mode tabs in the deferred register.
     * @param bus The event bus.
     */
    public static void register(IEventBus bus)
    {
        REGISTER.register(bus);
    }
}
