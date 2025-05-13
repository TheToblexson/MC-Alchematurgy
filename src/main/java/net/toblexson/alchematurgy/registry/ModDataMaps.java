package net.toblexson.alchematurgy.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.toblexson.alchematurgy.Alchematurgy;

import java.util.Map;

public class ModDataMaps
{
    public record Essences(float air, float earth, float fire, float water, float life, float magic)
    {
        public static final Map<String, DeferredItem<Item>> NAME_TO_ESSENCE = Map.of(
                "air", ModItems.BOTTLED_AIR_ESSENCE,
                "earth", ModItems.BOTTLED_EARTH_ESSENCE,
                "fire", ModItems.BOTTLED_WATER_ESSENCE,
                "water", ModItems.BOTTLED_WATER_ESSENCE,
                "life", ModItems.BOTTLED_LIFE_ESSENCE,
                "magic", ModItems.BOTTLED_MAGIC_ESSENCE);

        public static final Map<String, Integer> NAME_TO_INDEX = Map.of(
                "air", 0,
                "earth", 1,
                "fire", 2,
                "water", 3,
                "life", 4,
                "magic", 5);

        public static final Map<Integer, DeferredItem<Item>> INDEX_TO_ITEM = Map.of(
                0, ModItems.BOTTLED_AIR_ESSENCE,
                1, ModItems.BOTTLED_EARTH_ESSENCE,
                2, ModItems.BOTTLED_WATER_ESSENCE,
                3, ModItems.BOTTLED_WATER_ESSENCE,
                4, ModItems.BOTTLED_LIFE_ESSENCE,
                5, ModItems.BOTTLED_MAGIC_ESSENCE);

        public static final Codec<Essences> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.FLOAT.fieldOf("air").forGetter(Essences::air),
                Codec.FLOAT.fieldOf("earth").forGetter(Essences::earth),
                Codec.FLOAT.fieldOf("fire").forGetter(Essences::fire),
                Codec.FLOAT.fieldOf("water").forGetter(Essences::water),
                Codec.FLOAT.fieldOf("life").forGetter(Essences::life),
                Codec.FLOAT.fieldOf("magic").forGetter(Essences::magic))
                .apply(inst, Essences::new));

        public Map<String, Float> all()
        {
            return Map.of("air", air, "earth", earth, "fire", fire,
                          "water", water, "life", life, "magic", magic);
        }
    }

    public static final DataMapType<Item, Essences> ESSENCES = DataMapType.builder(
                    Alchematurgy.modLoc("essences"), Registries.ITEM, Essences.CODEC)
            .build();
}
