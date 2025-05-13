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
import net.toblexson.alchematurgy.registry.*;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalCrucibleMenu;
import org.jetbrains.annotations.Nullable;

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

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;
    private int fluidAmount = 0;
    private int maxFluid = 4;
    private int fuelLevel = 0;
    private int maxFuel = 0;

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
                    case DATA_WATER_AMOUNT_SLOT -> AlchemicalCrucibleBlockEntity.this.fluidAmount;
                    case DATA_MAX_WATER_SLOT -> AlchemicalCrucibleBlockEntity.this.maxFluid;
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
                    case DATA_WATER_AMOUNT_SLOT: AlchemicalCrucibleBlockEntity.this.fluidAmount = value;
                    case DATA_MAX_WATER_SLOT: AlchemicalCrucibleBlockEntity.this.maxFluid = value;
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

    /**
     * Add water to the crucible if it is not already full.
     * @return true if water has been added, false if not.
     */
    public boolean tryAddWater()
    {
        if (fluidAmount >= maxFluid)
            return false;
        fluidAmount = maxFluid;
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
        boolean updateLitStatus = false;
        boolean hasChanged = false;
        ModDataMaps.Essences essences = inventory.getStackInSlot(INPUT_SLOT).getItemHolder().getData(ModDataMaps.ESSENCES);
        //else reset progress
        if (essences != null)
        {
            ItemStack result = new ItemStack(ModItems.BOTTLED_MIXED_ESSENCE.get());
            result.set(ModDataComponents.ESSENCES, essences);
            if (canCraft(result))
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
                    //if progress has reached the required amount, craft
                    if (progress >= maxProgress)
                    {
                        finishCraft(result);
                        progress = 0;
                        hasChanged = true;
                    }
                    progress++;
                }
                else //there is no fuel
                    progress = 0;
            }
            else progress = 0;
        }
        else progress = 0;
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

    /**
     * Burn a fuel item from the fuel slot, increasing the fuel level.
     */
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
     * Complete the crafting operation. Removing from the input and adding to the result.
     */
    private void finishCraft(ItemStack result)
    {
        int newCount = inventory.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount();
        result.setCount(newCount);
        fluidAmount--;
        inventory.extractItem(BOTTLE_SLOT, 1, false);
        inventory.extractItem(INPUT_SLOT, 1, false);
        inventory.setStackInSlot(OUTPUT_SLOT, result);
    }

    /**
     * Check to see if there is the required water, bottle and space to craft.
     * @return Whether crafting is possible.
     */
    private boolean canCraft(ItemStack result)
    {
        return fluidAmount > 0 && hasBottle() && canOutput(result);
    }

    /**
     * Check if there is a glass bottle in the bottle slot.
     * @return If there is a glass bottle in the bottle slot.
     */
    private boolean hasBottle()
    {
        return inventory.getStackInSlot(BOTTLE_SLOT).is(Items.GLASS_BOTTLE);
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
        ModDataMaps.Essences stackEssences = stack.get(ModDataComponents.ESSENCES);
        ModDataMaps.Essences resultEssences = result.get(ModDataComponents.ESSENCES);
        return stack.getItem() == result.getItem() && stackEssences == resultEssences && outputCount <= maxCount;
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
        tag.putInt("water_amount", fluidAmount);
        tag.putInt("max_water", maxFluid);
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
        fluidAmount = tag.getInt("water_amount");
        maxFluid = tag.getInt("max_water");
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
