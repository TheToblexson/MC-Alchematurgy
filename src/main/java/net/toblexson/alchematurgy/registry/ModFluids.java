package net.toblexson.alchematurgy.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.world.fluid.MixedEssenceFluid;

public class ModFluids
{
    public static final DeferredRegister<Fluid> REGISTER = DeferredRegister.create(Registries.FLUID, Alchematurgy.MOD_ID);

    public static final Holder<Fluid> MIXED_ESSENCE = REGISTER.register("mixed_essence", MixedEssenceFluid::new);

    public static void register(IEventBus bus)
    {
        REGISTER.register(bus);
    }
}
