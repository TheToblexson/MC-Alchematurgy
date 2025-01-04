package net.toblexson.alchematurgy;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

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

    /**
     * Direct reference to a SLF4J logger
     */
    private static final Logger LOGGER = LogUtils.getLogger();

    /** The main constructor for the mod
     * @param modEventBus The Mod Event Bus
     * @param modContainer The Mod Container
     */
    public Alchematurgy(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

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
