package net.toblexson.alchematurgy.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalCrucibleBlockEntity;

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
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Alchematurgy.MOD_ID);

    public static final Supplier<BlockEntityType<AlchemicalCrucibleBlockEntity>> ALCHEMICAL_CRUCIBLE = BLOCK_ENTITY_TYPES.register("alchemical_crucible_block_entity",
                                                                                                                                   () -> BlockEntityType.Builder.of(AlchemicalCrucibleBlockEntity::new, ModBlocks.ALCHEMICAL_CRUCIBLE.get()).build(null));

    /**
     * Register all the block entities in the deferred register.
     * @param bus The Event Bus.
     */
    public static void register(IEventBus bus)
    {
        BLOCK_ENTITY_TYPES.register(bus);
    }
}

