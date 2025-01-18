package net.toblexson.alchematurgy.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.registry.ModBlockEntityTypes;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalCrucibleMenu;

/**
 * The block entity for the Alchemical Crucible
 */
public class AlchemicalCrucibleBlockEntity extends BaseContainerBlockEntity
{
    public static final int INVENTORY_SIZE = 4;
    public static final int INPUT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int BOTTLE_SLOT = 2;
    public static final int OUTPUT_SLOT = 3;

    /**
     * The item stack list.
     */
    private ItemStackHandler stackHandler = new ItemStackHandler(INVENTORY_SIZE);

    /**
     * Create the block entity
     * @param pos The block position.
     * @param state The block state.
     */
    public AlchemicalCrucibleBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntityTypes.ALCHEMICAL_CRUCIBLE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.saveAdditional(tag, provider);
        ContainerHelper.saveAllItems(tag,getItems(),provider);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider)
    {
        super.loadAdditional(tag, provider);
        NonNullList<ItemStack> items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, provider);
        setItems(items);
    }

    /**
     * The size of the container.
     * @return The number of slots in the container.
     */
    @Override
    public int getContainerSize()
    {
        return INVENTORY_SIZE;
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
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i < stackHandler.getSlots(); i++)
        {
            list.add(stackHandler.getStackInSlot(i));
        }
        return list;
    }

    /**
     * Set the item list.
     * @param items the new item list.
     */
    @Override
    protected void setItems(NonNullList<ItemStack> items)
    {
        this.stackHandler = new ItemStackHandler(items);
    }

    /**
     * Create the associated menu.
     * @param containerID The container ID.
     * @param playerInventory The player inventory.
     * @return The associated menu.
     */
    @Override
    protected AbstractContainerMenu createMenu(int containerID, Inventory playerInventory)
    {
        ContainerLevelAccess access = level == null ? ContainerLevelAccess.NULL : ContainerLevelAccess.create(level, worldPosition);
        return new AlchemicalCrucibleMenu(containerID, playerInventory, access, stackHandler);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemicalCrucibleBlockEntity blockEntity)
    {
        //todo - not working? Why is it air??
        ItemStack stack = blockEntity.getItem(INPUT_SLOT);
        Alchematurgy.LOGGER.debug(stack.toString());
    }
}
