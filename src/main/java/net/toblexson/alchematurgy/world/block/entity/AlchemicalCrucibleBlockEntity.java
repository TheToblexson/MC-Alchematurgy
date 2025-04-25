package net.toblexson.alchematurgy.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.toblexson.alchematurgy.registry.ModBlockEntityTypes;
import net.toblexson.alchematurgy.registry.ModBlocks;
import net.toblexson.alchematurgy.registry.ModItems;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalCrucibleMenu;
import org.jetbrains.annotations.Nullable;

/**
 * The block entity for the Alchemical Crucible
 */
public class AlchemicalCrucibleBlockEntity extends BlockEntity implements MenuProvider
{
    private static final int INVENTORY_SIZE = 4;
    public static final int INPUT_SLOT = 0;
    public static final int FUEL_SLOT = 1;
    public static final int BOTTLE_SLOT = 2;
    public static final int OUTPUT_SLOT = 3;

    public static final int DATA_COUNT = 2;
    public static final int DATA_PROGRESS_SLOT = 0;
    public static final int DATA_MAX_PROGRESS_SLOT = 1;

    public ItemStackHandler inventory = new ItemStackHandler(INVENTORY_SIZE)
    {
        /**
         * Updates the server when the inventory is modified.
         * @param slot The slot that has been modified.
         */
        @Override
        protected void onContentsChanged(int slot)
        {
            setChanged();
            assert level != null;
            if(!level.isClientSide())
                level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
        }
    };

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;

    /**
     * Create an instance of an Alchemical Crucible block entity.
     * @param pos The position of the block.
     * @param state The block state that is hosting the block entity.
     */
    public AlchemicalCrucibleBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntityTypes.ALCHEMICAL_CRUCIBLE.get(), pos, state);
        data = new ContainerData()
        {
            @Override
            public int get(int index)
            {
                return switch (index)
                {
                    case DATA_PROGRESS_SLOT -> AlchemicalCrucibleBlockEntity.this.progress;
                    case DATA_MAX_PROGRESS_SLOT -> AlchemicalCrucibleBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value)
            {
                switch (index)
                {
                    case DATA_PROGRESS_SLOT: AlchemicalCrucibleBlockEntity.this.progress = value;
                    case DATA_MAX_PROGRESS_SLOT: AlchemicalCrucibleBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount()
            {
                return DATA_COUNT;
            }
        };
    }

    /**
     * The ticking method for the block entity.
     * @param level The current level.
     * @param pos The block position.
     * @param state The block state.
     */
    public void tick(Level level, BlockPos pos, BlockState state)
    {
        //TODO implement recipes (canCraft needs to change so that it gets to max progress and then checks if it can output because there can be multiple output options.
        if(canCraft())
        {
            progress++;
            setChanged(level, pos, state);
            if (progress >= maxProgress)
            {
                craftItem();
                progress = 0;
            }
        }
        else
            progress = 0;
    }

    /**
     * Complete the crafting operation. Removing from the input and adding to the output.
     */
    private void craftItem()
    {
        ItemStack result = new ItemStack(ModItems.ELEMENT_EARTH.get());
        int newCount = inventory.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount();
        inventory.extractItem(INPUT_SLOT, 1, false);
        inventory.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(), newCount));
    }

    /**
     * Check to see if there is a recipe and space to craft.
     * @return Whether crafting is possible.
     */
    private boolean canCraft()
    {
        //TODO implement recipes
        ItemStack output = new ItemStack(ModItems.ELEMENT_EARTH.get());
        return !inventory.getStackInSlot(INPUT_SLOT).isEmpty() && canOutput(output);
    }

    private boolean canOutput(ItemStack output)
    {
        ItemStack stack = inventory.getStackInSlot(OUTPUT_SLOT);
        if (stack.isEmpty())
            return true;
        int maxCount = stack.getMaxStackSize();
        int outputCount = stack.getCount() + output.getCount();
        return stack.getItem() == output.getItem() && outputCount <= maxCount;
    }

    /**
     * Drops the inventory using the Containers helper method.
     */
    public void dropInventory()
    {
        SimpleContainer container = new SimpleContainer(inventory.getSlots());
        for(int i = 0; i < inventory.getSlots(); i++)
            container.setItem(i, inventory.getStackInSlot(i));
        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, container);
    }

    /**
     * Save additional data to the tag.
     * @param tag The tag being written to.
     * @param registries The registries.
     */
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.saveAdditional(tag, registries);
        tag.put("inventory", inventory.serializeNBT(registries));
        tag.putInt("progress", progress);
        tag.putInt("max_progress", maxProgress);
    }

    /**
     * Read additional data from the tag.
     * @param tag The tag being read from.
     * @param registries The registries.
     */
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
    {
        super.loadAdditional(tag, registries);
        inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("progress");
        maxProgress = tag.getInt("max_progress");
    }

    /**
     * Get the inventory's display name.
     * @return The display name.
     */
    @Override
    public Component getDisplayName()
    {
        return ModBlocks.ALCHEMICAL_CRUCIBLE.get().getName();
    }

    /**
     * Create the menu.
     * @param containerID The ID of the container.
     * @param inventory The inventory of the player.
     * @param player The player who has interacted with the block.
     * @return The new menu instance.
     */
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player)
    {
        return new AlchemicalCrucibleMenu(containerID, inventory, this, data);
    }

    /**
     * Create an update packet.
     * @return The new client-bound block entity packet.
     */
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    /**
     * Create an update tag.
     * @param registries The registries.
     * @return The new compound tag.
     */
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries)
    {
        return saveWithoutMetadata(registries);
    }
}
