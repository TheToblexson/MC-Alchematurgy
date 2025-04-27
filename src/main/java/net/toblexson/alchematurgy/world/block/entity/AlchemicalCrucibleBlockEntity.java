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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.toblexson.alchematurgy.Alchematurgy;
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

    public static final int DATA_COUNT = 6;
    public static final int DATA_PROGRESS_SLOT = 0;
    public static final int DATA_MAX_PROGRESS_SLOT = 1;
    public static final int DATA_WATER_AMOUNT_SLOT = 2;
    public static final int DATA_MAX_WATER_SLOT = 3;
    public static final int DATA_FUEL_LEVEL_SLOT = 4;
    public static final int DATA_MAX_FUEL_SLOT = 5;

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
    private int maxProgress = 50; //200
    private int waterAmount = 0;
    private int maxWater = 4;
    private int fuelLevel = 0;
    private int maxFuel = 0;

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
                    case DATA_WATER_AMOUNT_SLOT -> AlchemicalCrucibleBlockEntity.this.waterAmount;
                    case DATA_MAX_WATER_SLOT -> AlchemicalCrucibleBlockEntity.this.maxWater;
                    case DATA_FUEL_LEVEL_SLOT -> AlchemicalCrucibleBlockEntity.this.fuelLevel;
                    case DATA_MAX_FUEL_SLOT -> AlchemicalCrucibleBlockEntity.this.maxFuel;
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
                    case DATA_WATER_AMOUNT_SLOT: AlchemicalCrucibleBlockEntity.this.waterAmount = value;
                    case DATA_MAX_WATER_SLOT: AlchemicalCrucibleBlockEntity.this.maxWater = value;
                    case DATA_FUEL_LEVEL_SLOT: AlchemicalCrucibleBlockEntity.this.fuelLevel = value;
                    case DATA_MAX_FUEL_SLOT: AlchemicalCrucibleBlockEntity.this.maxFuel = value;
                }
            }

            @Override
            public int getCount()
            {
                return DATA_COUNT;
            }
        };
    }

    public boolean tryAddWater()
    {
        if (waterAmount >= maxWater)
            return false;
        waterAmount = maxWater;
        return true;
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
        boolean updateLitStatus = false;
        boolean hasChanged = false;
        if (canCraft())
        {
            //if there is no fuel, burn fuel
            if (fuelLevel <= 0)
            {
                maxFuel = 0;
                burnFuel();
                updateLitStatus = true;
            }
            //if there is fuel, craft
            if (fuelLevel > 0)
            {
                progress++;
                //if progress has reached the required amount, craft
                if (progress >= maxProgress)
                {
                    craftItem();
                    progress = 0;
                    hasChanged = true;
                }
            }
        }
        //else reset progress
        else
            progress = 0;
        //if lit, decrease fuel
        if (fuelLevel > 1)
            fuelLevel--;
        else
        {
            fuelLevel = 0;
            updateLitStatus = true;
        }
        //if fuel has changed, change state and update
        if (updateLitStatus)
        {
            if (fuelLevel > 0 && !state.getValue(BlockStateProperties.LIT))
            {
                Alchematurgy.LOGGER.debug("Setting Block State to LIT");
                state = state.setValue(BlockStateProperties.LIT, true);
                Alchematurgy.LOGGER.debug("State: {}", state);
                level.setBlock(pos, state, 3);
                hasChanged = true;
            }
            else if (fuelLevel <= 0 && state.getValue(BlockStateProperties.LIT))
            {
                Alchematurgy.LOGGER.debug("Setting Block State to UNLIT");
                state = state.setValue(BlockStateProperties.LIT, false);
                level.setBlock(pos, state, 3);
                hasChanged = true;
            }
        }
        if (hasChanged)
            setChanged(level, pos, state);
    }

    private void burnFuel()
    {
        if (inventory.getStackInSlot(FUEL_SLOT).getBurnTime(null) > 0)
        {
            maxFuel = inventory.getStackInSlot(FUEL_SLOT).getBurnTime(null);
            fuelLevel = maxFuel;
            inventory.extractItem(FUEL_SLOT, 1, false);
        }
    }

    /**
     * Complete the crafting operation. Removing from the input and adding to the output.
     */
    private void craftItem()
    {
        ItemStack result = new ItemStack(ModItems.BOTTLED_EARTH_ESSENCE.get());
        int newCount = inventory.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount();
        waterAmount--;
        inventory.extractItem(BOTTLE_SLOT, 1, false);
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
        ItemStack output = new ItemStack(ModItems.BOTTLED_EARTH_ESSENCE.get());
        return waterAmount > 0 && hasBottle() && !inventory.getStackInSlot(INPUT_SLOT).isEmpty() && canOutput(output);
    }

    private boolean hasBottle()
    {
        return inventory.getStackInSlot(BOTTLE_SLOT).is(Items.GLASS_BOTTLE);
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
        tag.putInt("water_amount", waterAmount);
        tag.putInt("max_water", maxWater);
        tag.putInt("fuel_level", fuelLevel);
        tag.putInt("max_fuel", maxFuel);
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
        waterAmount = tag.getInt("water_amount");
        maxWater = tag.getInt("max_water");
        fuelLevel = tag.getInt("fuel_level");
        maxFuel = tag.getInt("max_fuel");
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
