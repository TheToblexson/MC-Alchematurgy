package net.toblexson.alchematurgy.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.toblexson.alchematurgy.registry.ModBlockEntityTypes;

/**
 * The block entity for the Alchemical Crucible
 */
public class AlchemicalCrucibleBlockEntity extends BaseContainerBlockEntity
{
    /**
     * The item stack list.
     */
    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private final ItemStackHandler stackHandler = new ItemStackHandler(items);

    /**
     * Create the block entity
     * @param pos The block position.
     * @param state The block state.
     */
    public AlchemicalCrucibleBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntityTypes.ALCHEMICAL_CRUCIBLE.get(), pos, state);
    }

    /**
     * The size of the container.
     * @return The number of slots in the container.
     */
    @Override
    public int getContainerSize()
    {
        return items.size();
    }

    /**
     * The menu's display name.
     * @return The name component.
     */
    @Override
    protected Component getDefaultName()
    {
        return Component.translatable("block.alchematurgy.alchemical_crucible");
    }

    /**
     * Get the item list.
     * @return the item list.
     */
    @Override
    protected NonNullList<ItemStack> getItems()
    {
        return items;
    }

    /**
     * Set the item list.
     * @param items the new item list.
     */
    @Override
    protected void setItems(NonNullList<ItemStack> items)
    {
        this.items = items;
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory)
    {
        return null;
    }
}
