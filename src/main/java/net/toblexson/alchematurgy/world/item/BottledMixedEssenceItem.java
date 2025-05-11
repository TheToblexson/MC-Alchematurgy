package net.toblexson.alchematurgy.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.toblexson.alchematurgy.registry.ModDataComponents;
import net.toblexson.alchematurgy.registry.ModDataMaps;

import java.util.List;

public class BottledMixedEssenceItem extends Item
{
    public BottledMixedEssenceItem()
    {
        super(new Item.Properties());
    }

    /**
     * add to the hover text (tool tip)
     * @param stack The current stack.
     * @param context The tooltip context.
     * @param components The current components in the tooltip.
     * @param flag the tooltip flag.
     */
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag)
    {
        ModDataMaps.Essences essences = stack.get(ModDataComponents.ESSENCES);
        if (essences != null)
        {
            components.add(Component.literal("air: " + essences.air()));
            components.add(Component.literal("earth: " + essences.earth()));
            components.add(Component.literal("fire: " + essences.fire()));
            components.add(Component.literal("water: " + essences.water()));
            components.add(Component.literal("life: " + essences.life()));
            components.add(Component.literal("magic: " + essences.magic()));
        }
        super.appendHoverText(stack, context, components, flag);
    }
}
