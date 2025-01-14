package net.toblexson.alchematurgy;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.toblexson.alchematurgy.registry.ModBlockEntityTypes;
import net.toblexson.alchematurgy.registry.ModBlocks;
import net.toblexson.alchematurgy.registry.ModCreativeModeTabs;
import net.toblexson.alchematurgy.registry.ModItems;

/**
 * The main class for Alchematurgy
 */
@Mod(Alchematurgy.MOD_ID)
public class Alchematurgy
{
    /**
     * Alchematurgy's mod ID.
     */
    public static final String MOD_ID = "alchematurgy";

    /** The main constructor for the mod
     * @param bus The Mod Event Bus
     * @param modContainer The Mod Container
     */
    public Alchematurgy(IEventBus bus, ModContainer modContainer)
    {
        // Register the event listeners
        bus.addListener(this::commonSetup);

        // Register the deferred registers
        ModCreativeModeTabs.register(bus);
        ModItems.register(bus);
        ModBlocks.register(bus);
        ModBlockEntityTypes.register(bus);

        // Register ModConfigSpec so that FML can create and load the config file
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    /** Common Setup Event listener
     * @param event The Common Setup Event
     */
    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }
}
