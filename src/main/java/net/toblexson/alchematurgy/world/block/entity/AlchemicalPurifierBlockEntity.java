package net.toblexson.alchematurgy.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.toblexson.alchematurgy.Essence;
import net.toblexson.alchematurgy.registry.ModBlockEntityTypes;
import net.toblexson.alchematurgy.registry.ModBlocks;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalPurifierMenu;
import net.toblexson.alchematurgy.world.item.BottledEssenceItem;
import org.jetbrains.annotations.Nullable;

public class AlchemicalPurifierBlockEntity extends ModMenuBlockEntity
{
    public static final int INVENTORY_SIZE = 2;
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    public static final int DATA_COUNT = 2;
    public static final int DATA_PROGRESS_SLOT = 0;
    public static final int DATA_MAX_PROGRESS_SLOT = 1;

    private int progress = 0;
    private int maxProgress = 200;

    public AlchemicalPurifierBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntityTypes.ALCHEMICAL_PURIFIER.get(), pos, state, INVENTORY_SIZE);
    }

    /**
     * Initialise the container data for the container.
     * @return the initialised container data.
     */
    @Override
    protected ContainerData initialiseData()
    {
        return new ContainerData()
        {
            @Override
            public int get(int index)
            {
                return switch (index)
                {
                    case DATA_PROGRESS_SLOT -> AlchemicalPurifierBlockEntity.this.progress;
                    case DATA_MAX_PROGRESS_SLOT -> AlchemicalPurifierBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value)
            {
                switch (index)
                {
                    case DATA_PROGRESS_SLOT: AlchemicalPurifierBlockEntity.this.progress = value;
                    case DATA_MAX_PROGRESS_SLOT: AlchemicalPurifierBlockEntity.this.maxProgress = value;
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
        ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
        if (!input.isEmpty() && input.getItem() instanceof BottledEssenceItem)
        {
            Item resultItem = getResult(input);
            if (resultItem != null)
            {
                ItemStack result = new ItemStack(resultItem);
                if (canOutput(result))
                {
                    if (progress >= maxProgress)
                    {
                        finishCraft(result);
                        progress = 0;
                    }
                    progress++;
                }
            }
            else progress = 0;
        }
        else progress = 0;
    }

    @Nullable
    private Item getResult(ItemStack input)
    {
        Essence essence = ((BottledEssenceItem)input.getItem()).getEssence();
        return Essence.getPureEssence(essence.index);
    }

    /**
     * Complete the crafting operation. Removing from the input and adding to the result.
     */
    private void finishCraft(ItemStack result)
    {
        int newCount = inventory.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount();
        result.setCount(newCount);
        inventory.extractItem(INPUT_SLOT, 1, false);
        inventory.setStackInSlot(OUTPUT_SLOT, result);
    }

    /**
     * Check if the crafting result can be put into the output slot.
     * @param result If there is space in the output slot for the result.
     * @return The crafting result.
     */
    private boolean canOutput(ItemStack result)
    {
        ItemStack stack = inventory.getStackInSlot(OUTPUT_SLOT);
        if (stack.isEmpty())
            return true;
        int maxCount = stack.getMaxStackSize();
        int outputCount = stack.getCount() + 1;
        return stack.getItem() == result.getItem() && outputCount <= maxCount;
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
        return ModBlocks.ALCHEMICAL_PURIFIER.get().getName();
    }

    /**
     * Create the menu.
     * @param containerID The ID of the container.
     * @param inventory The inventory of the player.
     * @param player The player who has interacted with the block.
     * @return The new menu instance.
     */
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player)
    {
        return new AlchemicalPurifierMenu(containerID, inventory, this, data);
    }
}
