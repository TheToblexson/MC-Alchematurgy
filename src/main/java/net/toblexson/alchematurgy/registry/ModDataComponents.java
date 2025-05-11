package net.toblexson.alchematurgy.registry;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.toblexson.alchematurgy.Alchematurgy;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ModDataComponents
{
    public static final DeferredRegister<DataComponentType<?>> REGISTER = DeferredRegister.createDataComponents(Alchematurgy.MOD_ID);

    public static Supplier<DataComponentType<ModDataMaps.Essences>> ESSENCES = register("essences", builder -> builder.persistent(ModDataMaps.Essences.CODEC));

    private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> operator)
    {
        return REGISTER.register(name, () -> operator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus bus)
    {
        REGISTER.register(bus);
    }
}
