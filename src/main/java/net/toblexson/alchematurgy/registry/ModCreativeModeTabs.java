package net.toblexson.alchematurgy.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;

import java.util.function.Supplier;

public class ModCreativeModeTabs
{
    /**
     * The Deferred Register for the mod's creative mode tabs
     */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Alchematurgy.MOD_ID);

    public static Supplier<CreativeModeTab> ALCHEMATURGY_TAB = CREATIVE_MODE_TABS.register("alchematurgy_tab", ()->
            CreativeModeTab.builder()
                    .icon(()-> new ItemStack(ModItems.TEST_ITEM.get()))
                    .title(Component.translatable("creativetab.alchematurgy.alchematurgy_tab"))
                    .displayItems((itemsDisplayParameters, output) ->
                    {
                        output.accept(ModItems.TEST_ITEM);
                        output.accept(ModBlocks.TEST_BLOCK);
                    }).build());

    /** Register all the creative mode tabs in the deferred register.
     * @param bus The Event Bus
     */
    public static void register(IEventBus bus)
    {
        CREATIVE_MODE_TABS.register(bus);
    }
}
