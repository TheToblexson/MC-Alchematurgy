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
import net.toblexson.alchematurgy.registry.ModDataComponents;
import net.toblexson.alchematurgy.registry.ModDataMaps.Essences;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalSeparatorMenu;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

//When crafted the water becomes Mixed Essence, and then is result into bottles.

public class AlchemicalSeparatorBlockEntity extends BlockEntity implements MenuProvider
{
    private static final int INVENTORY_SIZE = 2;
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    public static final int DATA_COUNT = 3;
    public static final int DATA_PROGRESS_SLOT = 0;
    public static final int DATA_MAX_PROGRESS_SLOT = 1;
    public static final int DATA_RESULT_ESSENCE_SLOT = 2;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private int resultEssence = -1;

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

    public AlchemicalSeparatorBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntityTypes.ALCHEMICAL_SEPARATOR.get(), pos, state);
        data = new ContainerData()
        {
            @Override
            public int get(int index)
            {
                return switch (index)
                {
                    case DATA_PROGRESS_SLOT -> AlchemicalSeparatorBlockEntity.this.progress;
                    case DATA_MAX_PROGRESS_SLOT -> AlchemicalSeparatorBlockEntity.this.maxProgress;
                    case DATA_RESULT_ESSENCE_SLOT -> AlchemicalSeparatorBlockEntity.this.resultEssence;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value)
            {
                switch (index)
                {
                    case DATA_PROGRESS_SLOT: AlchemicalSeparatorBlockEntity.this.progress = value;
                    case DATA_MAX_PROGRESS_SLOT: AlchemicalSeparatorBlockEntity.this.maxProgress = value;
                    case DATA_RESULT_ESSENCE_SLOT: AlchemicalSeparatorBlockEntity.this.resultEssence = value;
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
        Essences essences = inventory.getStackInSlot(INPUT_SLOT).get(ModDataComponents.ESSENCES.get());
        if (essences != null)
        {
            ItemStack result = getDominantEssence(essences);
            if (canOutput(result))
            {
                if (progress >= maxProgress)
                {
                    finishCraft(result);
                    progress = 0;
                    resultEssence = -1;
                }
                progress++;
            }
            else progress = 0;
        }
        else progress = 0;
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

    private ItemStack getDominantEssence(Essences essences)
    {
        if (resultEssence >= 0 && resultEssence < 6)
        {
            return new ItemStack(Essences.INDEX_TO_DIRTY_ITEM.get(resultEssence).get());
        }
        Iterator<Map.Entry<String, Float>> iterator = essences.all().entrySet().iterator();
        ArrayList<Map.Entry<String, Float>> dominantEssences = new ArrayList<>();
        float count = 0;
        while (iterator.hasNext())
        {
            Map.Entry<String, Float> entry = iterator.next();
            if (entry.getValue() > count)
            {
                count = entry.getValue();
                dominantEssences.clear();
                dominantEssences.add(entry);
            }
            else if (entry.getValue() == count)
                dominantEssences.add(entry);
        }
        int test = dominantEssences.size();
        //Randomly pick from the list
        int index = this.level.random.nextInt(dominantEssences.size());
        resultEssence = Essences.NAME_TO_INDEX.get(dominantEssences.get(index).getKey());
        return new ItemStack(Essences.NAME_TO_ESSENCE.get(dominantEssences.get(index).getKey()).get());
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
        return ModBlocks.ALCHEMICAL_SEPARATOR.get().getName();
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
        return new AlchemicalSeparatorMenu(containerID, inventory, this, data);
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
