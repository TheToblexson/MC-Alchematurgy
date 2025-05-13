package net.toblexson.alchematurgy.world.inventory.menu;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.function.Function;

public class ModSlotItemHandler extends SlotItemHandler
{
    private final Function<ItemStack, Boolean> test;

    public ModSlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition, Function<ItemStack,Boolean> test)
    {
        super(itemHandler, index, xPosition, yPosition);
        this.test = test;
    }

    @Override
    public boolean mayPlace(ItemStack stack)
    {
        return test.apply(stack);
    }
}
