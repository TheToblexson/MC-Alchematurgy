package net.toblexson.alchematurgy;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import net.toblexson.alchematurgy.registry.*;
import net.toblexson.alchematurgy.world.inventory.screen.AlchemicalCrucibleScreen;
import org.slf4j.Logger;

/* Work on Separator
Input - Bottle of Mixed Essence
Outputs - dirty? bottle of primary essence
 */

/* Work on Purifier
Input - Bottle of Impure Essence
Consume - Alchemical Filter (durability)
Output - Bottle of Pure Essence
 */

/* Work on Concentrator
Input - Bottle of Pure Essence
Consume - Fuel?
Output - Bottle of Concentrated Essence
 */

/* Work on Fabricator
Input - Any item with Essence data
Consume - Appropriate amount of Essence
Output - Duplicate item
 */

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

    public static final Logger LOGGER = LogUtils.getLogger();

    /** The main constructor for the mod
     * @param bus The Mod Event Bus
     * @param modContainer The Mod Container
     */
    public Alchematurgy(IEventBus bus, ModContainer modContainer)
    {
        // Register the event listeners
        bus.addListener(this::commonSetup);
        bus.addListener(this::registerScreens);
        bus.addListener(this::registerDataMapTypes);

        // Register the deferred registers
        ModCreativeModeTabs.register(bus);
        ModItems.register(bus);
        ModBlocks.register(bus);
        ModBlockEntityTypes.register(bus);
        ModMenuTypes.register(bus);
        ModDataComponents.register(bus);

        // Register ModConfigSpec so that FML can create and load the config file
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void registerDataMapTypes(RegisterDataMapTypesEvent event)
    {
        event.register(ModDataMaps.ESSENCES);
    }

    /** Common Setup Event listener
     * @param event The Common Setup Event
     */
    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    /**
     * Register container screens
     * @param event The register menu screens event.
     */
    private void registerScreens(RegisterMenuScreensEvent event)
    {
        event.register(ModMenuTypes.ALCHEMICAL_CRUCIBLE_MENU.get(), AlchemicalCrucibleScreen::new);
    }

    /**
     * Convert a relative assets path into a mod-specific resource location.
     * @param path The path
     * @return The complete resource location
     */
    public static ResourceLocation modLoc(String path)
    {
        return ResourceLocation.fromNamespaceAndPath(Alchematurgy.MOD_ID, path);
    }
}
