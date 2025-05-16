package net.toblexson.alchematurgy.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalCrucibleBlockEntity;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalPurifierBlockEntity;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalSeparatorBlockEntity;

import java.util.function.Supplier;

/**
 * Alchematurgy's Block Entity registration.
 */
@SuppressWarnings("DataFlowIssue")
public class ModBlockEntityTypes
{
    /**
     * The Deferred Register for all the block entities.
     */
    public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Alchematurgy.MOD_ID);

    public static final Supplier<BlockEntityType<AlchemicalCrucibleBlockEntity>> ALCHEMICAL_CRUCIBLE = REGISTER.register("alchemical_crucible", () ->
            BlockEntityType.Builder.of(AlchemicalCrucibleBlockEntity::new,
                                         ModBlocks.ALCHEMICAL_CRUCIBLE.get()).build(null));

    public static final Supplier<BlockEntityType<AlchemicalSeparatorBlockEntity>> ALCHEMICAL_SEPARATOR = REGISTER.register("alchemical_separator", () ->
            BlockEntityType.Builder.of(AlchemicalSeparatorBlockEntity::new, ModBlocks.ALCHEMICAL_SEPARATOR.get()).build(null));

    public static final Supplier<BlockEntityType<AlchemicalPurifierBlockEntity>> ALCHEMICAL_PURIFIER = REGISTER.register("alchemical_purifier", () ->
            BlockEntityType.Builder.of(AlchemicalPurifierBlockEntity::new, ModBlocks.ALCHEMICAL_PURIFIER.get()).build(null));

    /**
     * Register all the block entities in the deferred register.
     * @param bus The Event Bus.
     */
    public static void register(IEventBus bus)
    {
        REGISTER.register(bus);
    }
}

