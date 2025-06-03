package net.toblexson.alchematurgy.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.toblexson.alchematurgy.Alchematurgy;
import net.toblexson.alchematurgy.registry.*;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalCrucibleMenu;
import org.jetbrains.annotations.Nullable;

public class AlchemicalCrucibleBlockEntity extends ModMenuBlockEntity implements WorldlyContainer, IFluidHandler
{
    public static final int INVENTORY_SIZE = 4;
    public static final int FUEL_SLOT = 0;
    public static final int BOTTLE_SLOT = 1;
    public static final int INPUT_SLOT = 2;
    public static final int OUTPUT_SLOT = 3;

    public static final int DATA_COUNT = 9;
    public static final int DATA_CRAFT_PROGRESS_SLOT = 0;
    public static final int DATA_MAX_CRAFT_PROGRESS_SLOT = 1;
    public static final int DATA_FLUID_AMOUNT_SLOT = 2;
    public static final int DATA_MAX_FLUID_SLOT = 3;
    public static final int DATA_FUEL_LEVEL_SLOT = 4;
    public static final int DATA_MAX_FUEL_SLOT = 5;
    public static final int DATA_IS_ESSENCE_SLOT = 6;
    public static final int DATA_TRANSFER_PROGRESS_SLOT = 7;
    public static final int DATA_MAX_TRANSFER_PROGRESS_SLOT = 8;

    private int craftProgress = 0;
    private int maxCraftProgress = 200;
    private int maxFluid = 1000; //in mb
    private int fuelLevel = 0;
    private int maxFuel = 0;
    private boolean isEssence = false;
    private int transferProgress = 0;
    private int maxTransferProgress = 50;

    private FluidStack fluidStack = FluidStack.EMPTY;

    public AlchemicalCrucibleBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntityTypes.ALCHEMICAL_CRUCIBLE.get(), pos, state, INVENTORY_SIZE);
    }

    /**
     * Initialise the container data for the container.
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
                    case DATA_CRAFT_PROGRESS_SLOT -> AlchemicalCrucibleBlockEntity.this.craftProgress;
                    case DATA_MAX_CRAFT_PROGRESS_SLOT -> AlchemicalCrucibleBlockEntity.this.maxCraftProgress;
                    case DATA_FLUID_AMOUNT_SLOT -> AlchemicalCrucibleBlockEntity.this.fluidStack.getAmount();
                    case DATA_MAX_FLUID_SLOT -> AlchemicalCrucibleBlockEntity.this.maxFluid;
                    case DATA_FUEL_LEVEL_SLOT -> AlchemicalCrucibleBlockEntity.this.fuelLevel;
                    case DATA_MAX_FUEL_SLOT -> AlchemicalCrucibleBlockEntity.this.maxFuel;
                    case DATA_IS_ESSENCE_SLOT -> AlchemicalCrucibleBlockEntity.this.isEssence ? 1 : 0;
                    case DATA_TRANSFER_PROGRESS_SLOT -> AlchemicalCrucibleBlockEntity.this.transferProgress;
                    case DATA_MAX_TRANSFER_PROGRESS_SLOT -> AlchemicalCrucibleBlockEntity.this.maxTransferProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value)
            {
                switch (index)
                {
                    case DATA_CRAFT_PROGRESS_SLOT -> AlchemicalCrucibleBlockEntity.this.craftProgress = value;
                    case DATA_MAX_CRAFT_PROGRESS_SLOT -> AlchemicalCrucibleBlockEntity.this.maxCraftProgress = value;
                    case DATA_FLUID_AMOUNT_SLOT -> AlchemicalCrucibleBlockEntity.this.fluidStack.setAmount(value);
                    case DATA_MAX_FLUID_SLOT -> AlchemicalCrucibleBlockEntity.this.maxFluid = value;
                    case DATA_FUEL_LEVEL_SLOT -> AlchemicalCrucibleBlockEntity.this.fuelLevel = value;
                    case DATA_MAX_FUEL_SLOT -> AlchemicalCrucibleBlockEntity.this.maxFuel = value;
                    case DATA_TRANSFER_PROGRESS_SLOT -> AlchemicalCrucibleBlockEntity.this.transferProgress = value;
                    case DATA_MAX_TRANSFER_PROGRESS_SLOT -> AlchemicalCrucibleBlockEntity.this.maxTransferProgress = value;
                    case DATA_IS_ESSENCE_SLOT -> isEssence = value > 0;
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
     */
    public boolean tryAddWater()
    {
        if (fluidStack.is(ModFluids.MIXED_ESSENCE) || fluidStack.getAmount() >= maxFluid)
            return false;
        fluidStack = new FluidStack(Fluids.WATER, maxFluid);
        isEssence = false;
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
        if (fuelLevel > 0) fuelLevel--;

        boolean updateLit = false;
        boolean hasChanged = false;
        ModDataMaps.Essences essences = inventory.getStackInSlot(INPUT_SLOT).getItemHolder().getData(ModDataMaps.ESSENCES);
        if (essences != null)
        {
            if (fluidStack.is(Fluids.WATER) && fluidStack.getAmount() >= maxFluid)
            {
                updateLit = burnFuel();
                if (fuelLevel > 0)
                {
                    if (craftProgress >= maxCraftProgress)
                    {
                        finishCraft(essences);
                        craftProgress = 0;
                        hasChanged = true;
                    }
                    else craftProgress++;
                }
                else craftProgress = 0;
            }
            else craftProgress = 0;
        }
        else craftProgress = 0;

        //if fuel has changed, change state and update
        if (updateLit)
        {
            if (fuelLevel > 0 && !state.getValue(BlockStateProperties.LIT))
            {
                state = state.setValue(BlockStateProperties.LIT, true);
                level.setBlock(pos, state, 3);
                hasChanged = true;
            }
            else if (fuelLevel <= 0 && state.getValue(BlockStateProperties.LIT))
            {
                state = state.setValue(BlockStateProperties.LIT, false);
                level.setBlock(pos, state, 3);
                hasChanged = true;
            }
        }
        if (hasChanged)
            setChanged(level, pos, state);

        if (canTransfer())
        {
            if (transferProgress >= maxTransferProgress)
            {
                transferToBottle();
                transferProgress = 0;
            }
            else transferProgress++;
        }
        else transferProgress = 0;
    }

    /**
     * Checks to see if essence can be transferred to a bottle
     * @return TRUE is essence can be transferred, FALSE if it cannot;
     */
    private boolean canTransfer()
    {
        ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);
        ModDataMaps.Essences fluidData = fluidStack.get(ModDataComponents.ESSENCES);
        ModDataMaps.Essences bottleData = output.get(ModDataComponents.ESSENCES);
        return fluidStack.is(ModFluids.MIXED_ESSENCE) && fluidStack.getAmount() >= 250 && //Checking fluid
                inventory.getStackInSlot(BOTTLE_SLOT).is(Items.GLASS_BOTTLE) && //Checking bottle slot
                (output.isEmpty() || output.is(ModItems.BOTTLED_MIXED_ESSENCE.get()) && fluidData == bottleData); //Checking output slot
    }


    /**
     * Transfers some of the essence into a bottle, consuming a bottle and some essence to output a mixed essence bottle.
     */
    private void transferToBottle()
    {
        ModDataMaps.Essences essences = fluidStack.get(ModDataComponents.ESSENCES);
        if (essences == null)
        {
            Alchematurgy.LOGGER.error("Error in Alchemical Crucible at pos {}. " +
                                              "Alchemical Crucible contains Mixed Essence with no essence data. " +
                                              "If this is a reoccurring error, please provide a bug report.", getBlockPos());
        }
        else
        {
            int count = inventory.getStackInSlot(OUTPUT_SLOT).getCount() + 1;
            ItemStack result = new ItemStack(ModItems.BOTTLED_MIXED_ESSENCE.get(), count);
            result.set(ModDataComponents.ESSENCES, essences);
            inventory.getStackInSlot(BOTTLE_SLOT).shrink(1);
            fluidStack.shrink(250);
            inventory.setStackInSlot(OUTPUT_SLOT,result);
            if (fluidStack.getAmount() <= 0)
            {
                fluidStack = FluidStack.EMPTY;
                fluidStack.remove(ModDataComponents.ESSENCES);
                isEssence = false;
            }
        }
    }

    /**
     * If there is no fuel left, burn a fuel item if present.
     * @return FALSE if no extra fuel has been burnt, TRUE if fuel has been burnt.
     */
    private boolean burnFuel()
    {
        if (fuelLevel > 0)
            return false;
        int fuel = inventory.getStackInSlot(FUEL_SLOT).getBurnTime(null);
        fuelLevel = fuel;
        maxFuel = fuel;
        inventory.extractItem(FUEL_SLOT, 1, false);
        return true;
    }

    /**
     * Complete the crafting operation. Removing from the input and adding to the result.
     * @param essences the essences from the input.
     */
    private void finishCraft(ModDataMaps.Essences essences)
    {
        inventory.extractItem(INPUT_SLOT, 1, false);

        fluidStack = new FluidStack(ModFluids.MIXED_ESSENCE, maxFluid);
        fluidStack.set(ModDataComponents.ESSENCES, essences);
        isEssence = true;
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
        if (fluidStack.getAmount() > 0)
            tag.put("fluid", fluidStack.save(registries));
        tag.putInt("craft_progress", craftProgress);
        tag.putInt("max_craft_progress", maxCraftProgress);
        tag.putInt("fill_progress", craftProgress);
        tag.putInt("max_fill_progress", maxCraftProgress);
        tag.putInt("max_fluid", maxFluid);
        tag.putInt("fuel_level", fuelLevel);
        tag.putInt("max_fuel", maxFuel);
        tag.putBoolean("is_essence", isEssence);
        tag.putInt("transfer_progress", transferProgress);
        tag.putInt("max_transfer_progress", maxTransferProgress);
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
        if (tag.getCompound("fluid").isEmpty())
            fluidStack = new FluidStack(Fluids.WATER, 0);
        else
            fluidStack = FluidStack.parseOptional(registries, tag.getCompound("fluid"));
        craftProgress = tag.getInt("progress");
        maxCraftProgress = tag.getInt("max_progress");
        maxFluid = tag.getInt("max_fluid");
        fuelLevel = tag.getInt("fuel_level");
        maxFuel = tag.getInt("max_fuel");
        isEssence = tag.getBoolean("is_essence");
        transferProgress = tag.getInt("transfer_progress");
        maxTransferProgress = tag.getInt("max_transfer_progress");
    }

    /**
     * Get the inventory's display name.
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
    @Override
    public AbstractContainerMenu createMenu(int containerID, Inventory inventory, Player player)
    {
        return new AlchemicalCrucibleMenu(containerID, inventory, this, data);
    }

    /**
     * Get the slots that can be accessed from the given side.
     * @param side The side being interacted with.
     */
    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[] {FUEL_SLOT, BOTTLE_SLOT, INPUT_SLOT};
    }

    /**
     * Returns {@code true} if automation can insert the given item in the given slot from the given side.
     * @param slot The inventory index to test inserting into.
     * @param stack The stack to insert.
     * @param direction The face that is being tested.
     */
    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction)
    {
        if (slot == OUTPUT_SLOT) return false;
        return canPlaceItem(slot, stack);
    }

    /**
     * Returns {@code true} if automation can place the given item in the given slot
     * @param slot The inventory index to test inserting into.
     * @param stack The stack to insert.
     */
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack)
    {

        return switch (slot)
        {
            case INPUT_SLOT ->
                    (inventory.getStackInSlot(slot).isEmpty() || inventory.getStackInSlot(slot).is(stack.getItem()));
            case BOTTLE_SLOT ->
                    (stack.is(Items.GLASS_BOTTLE) && inventory.getStackInSlot(slot).isEmpty() || inventory.getStackInSlot(slot).is(stack.getItem()));
            case FUEL_SLOT ->
                    (stack.getBurnTime(null) > 0 && inventory.getStackInSlot(slot).isEmpty() || inventory.getStackInSlot(slot).is(stack.getItem()));
            default -> false;
        };
    }

    /**
     * Returns {@code true} if automation can extract the given item in the given slot from the given side.
     * @param index The inventory index to test taking from.
     * @param stack The stack to insert.
     * @param direction The face that is being tested.
     */
    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction)
    {
        //Not working??
        return index == OUTPUT_SLOT || stack.is(Items.BUCKET);
    }

    /**
     * Get the size of the container's inventory.
     */
    @Override
    public int getContainerSize()
    {
        return AlchemicalCrucibleBlockEntity.INVENTORY_SIZE;
    }

    /**
     * Returns {@code true} if the inventory is empty.
     */
    @Override
    public boolean isEmpty()
    {
        for (int i = 0; i < inventory.getSlots(); i++)
        {
            if (!inventory.getStackInSlot(i).isEmpty())
                return false;
        }
        return true;
    }

    /**
     * Returns the stack in the given slot.
     * @param slot The slot index.
     */
    @Override
    public ItemStack getItem(int slot)
    {
        if (slot >= INVENTORY_SIZE || slot < 0)
            return ItemStack.EMPTY;
        return inventory.getStackInSlot(slot);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     * @param slot The slot index.
     * @param amount The amount to remove from the slot.
     */
    @Override
    public ItemStack removeItem(int slot, int amount)
    {
        int stackAmount = getItem(slot).getCount();
        if (stackAmount <= 0)
            return ItemStack.EMPTY;
        this.setChanged();
        if (stackAmount <= amount)
            return inventory.extractItem(slot, amount, false);
        return inventory.extractItem(slot, stackAmount, false);
    }

    /**
     * Removes a stack from the given slot and returns it.
     * @param slot The slot index.
     */
    @Override
    public ItemStack removeItemNoUpdate(int slot)
    {
        return inventory.extractItem(slot, getItem(slot).getCount(), false);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     * @param slot The slot index.
     * @param stack The item stack to insert.
     */
    @Override
    public void setItem(int slot, ItemStack stack)
    {
        switch (slot)
        {
            case INPUT_SLOT:
                inventory.setStackInSlot(slot, stack);
            case FUEL_SLOT:
                if (stack.getBurnTime(null) > 0)
                    inventory.setStackInSlot(slot, stack);
            case BOTTLE_SLOT:
                if (stack.is(Items.GLASS_BOTTLE))
                    inventory.setStackInSlot(slot, stack);
        }
    }

    /**
     * Returns {@code true} if the supplied player can use the container.
     * @param player The player using the container.
     */
    @Override
    public boolean stillValid(Player player)
    {
        return Container.stillValidBlockEntity(this, player);
    }

    /**
     *
     */
    @Override
    public void clearContent()
    {
        inventory = createInventory(INVENTORY_SIZE);
    }

    /**
     * Returns the number of fluid storage units ("tanks") available
     * @return The number of tanks available
     */
    @Override
    public int getTanks()
    {
        return 1;
    }

    /**
     * Returns the FluidStack in a given tank.
     * <p>
     * <strong>IMPORTANT:</strong> This FluidStack <em>MUST NOT</em> be modified. This method is not for
     * altering internal contents. Any implementers who are able to detect modification via this method
     * should throw an exception. It is ENTIRELY reasonable and likely that the stack returned here will be a copy.
     * @param tank Tank to query.
     * @return FluidStack in a given tank. FluidStack.EMPTY if the tank is empty.
     */
    @Override
    public FluidStack getFluidInTank(int tank)
    {
        return fluidStack.copy();
    }

    /**
     * Retrieves the maximum fluid amount for a given tank.
     * @param tank Tank to query.
     * @return The maximum fluid amount held by the tank.
     */
    @Override
    public int getTankCapacity(int tank)
    {
        return maxFluid;
    }

    /**
     * This function is a way to determine which fluids can exist inside a given handler. General purpose tanks will
     * basically always return TRUE for this.
     * @param tank  Tank to query for validity
     * @param stack Stack to test with for validity
     * @return TRUE if the tank can hold the FluidStack, not considering current state.
     */
    @Override
    public boolean isFluidValid(int tank, FluidStack stack)
    {
        return stack.is(Fluids.WATER);
    }

    /**
     * Fills fluid into internal tanks, distribution is left entirely to the IFluidHandler.
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be filled.
     * @param action   If SIMULATE, fill will only be simulated.
     * @return Amount of resource that was (or would have been, if simulated) filled.
     */
    @Override
    public int fill(FluidStack resource, FluidAction action)
    {
        int emptySpace = maxFluid - fluidStack.getAmount();
        int waterIn = resource.getAmount();
        int fillAmount = Math.min(waterIn, emptySpace);
        if (action.execute())
            fluidStack.grow(fillAmount);
        return fillAmount;
    }

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     * @param resource FluidStack representing the Fluid and maximum amount of fluid to be drained.
     * @param action   If SIMULATE, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     * simulated) drained.
     */
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action)
    {
        return drain(resource.getAmount(), action);
    }

    /**
     * Drains fluid out of internal tanks, distribution is left entirely to the IFluidHandler.
     * <p>
     * This method is not Fluid-sensitive.
     * @param maxDrain Maximum amount of fluid to drain.
     * @param action   If SIMULATE, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     * simulated) drained.
     */
    @Override
    public FluidStack drain(int maxDrain, FluidAction action)
    {
        int outputAmount = Math.min(maxDrain, fluidStack.getAmount());
        if (action.execute())
            fluidStack.shrink(outputAmount);
        return new FluidStack(Fluids.WATER, outputAmount);
    }
}
