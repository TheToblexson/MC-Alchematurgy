package net.toblexson.alchematurgy.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.toblexson.alchematurgy.Alchematurgy;

public class ModDataMaps
{
    public record Essences(float air, float earth, float fire, float water, float life, float magic)
    {
        public static final Codec<Essences> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.FLOAT.fieldOf("air").forGetter(Essences::air),
                Codec.FLOAT.fieldOf("earth").forGetter(Essences::earth),
                Codec.FLOAT.fieldOf("fire").forGetter(Essences::fire),
                Codec.FLOAT.fieldOf("water").forGetter(Essences::water),
                Codec.FLOAT.fieldOf("life").forGetter(Essences::life),
                Codec.FLOAT.fieldOf("magic").forGetter(Essences::magic))
                .apply(inst, Essences::new));
    }

    public static final DataMapType<Item, Essences> ESSENCES = DataMapType.builder(
                    Alchematurgy.modLoc("essences"), Registries.ITEM, Essences.CODEC)
            .build();
}
