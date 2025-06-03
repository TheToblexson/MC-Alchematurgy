package net.toblexson.alchematurgy.world.inventory.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.toblexson.alchematurgy.registry.ModBlocks;
import net.toblexson.alchematurgy.registry.ModMenuTypes;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalCrucibleBlockEntity;

import java.util.Objects;

public class AlchemicalCrucibleMenu extends ModMenu
{
    /**
     * Client-side constructor.
     * @param containerId The id of the container.
     * @param inventory The player's inventory.
     * @param data Extra data from a buffer.
     */
    public AlchemicalCrucibleMenu(int containerId, Inventory inventory, FriendlyByteBuf data)
    {
        this(containerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(data.readBlockPos())),
             new SimpleContainerData(AlchemicalCrucibleBlockEntity.DATA_COUNT));
    }

    /**
     * Server-side constructor.
     * @param containerId The id of the container.
     * @param inventory The player's inventory.
     * @param blockEntity The block entity that the menu is attached to.
     */
    public AlchemicalCrucibleMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData data)
    {
        super(ModMenuTypes.ALCHEMICAL_CRUCIBLE.get(), containerId, inventory, blockEntity, data, AlchemicalCrucibleBlockEntity.INVENTORY_SIZE);

        //input
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory,AlchemicalCrucibleBlockEntity.INPUT_SLOT, 55, 17));
        //fuel
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory,AlchemicalCrucibleBlockEntity.FUEL_SLOT,
                                            55, 53,  (stack) -> stack.getBurnTime(null) > 0));
        //bottle
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory,AlchemicalCrucibleBlockEntity.BOTTLE_SLOT,
                                            105, 17, (stack -> stack.is(Items.GLASS_BOTTLE))));
        //output
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory,AlchemicalCrucibleBlockEntity.OUTPUT_SLOT,
                                         105, 53, (stack) -> false));

        addDataSlots(data);
    }

    /**
     * If there is fluid in the block entity.
     * @return True if the fluid level is above 0.
     */
    public boolean hasFluid()
    {
        return data.get(AlchemicalCrucibleBlockEntity.DATA_FLUID_AMOUNT_SLOT) > 0;
    }

    /**
     * Calculates the amount of the water cover texture to render.
     * @return the height the water cover texture should be.
     */
    public int FluidAmount(int maxSize)
    {
        int fluidAmount = data.get(AlchemicalCrucibleBlockEntity.DATA_FLUID_AMOUNT_SLOT);
        int maxFluid = data.get(AlchemicalCrucibleBlockEntity.DATA_MAX_FLUID_SLOT);
        return (int)Math.floor(fluidAmount * ((float)maxSize / maxFluid));
    }

    /**
     * If the crucible should render the flames.
     * @return If the heat value is above zero.
     */
    public boolean isBurning()
    {
        return data.get(AlchemicalCrucibleBlockEntity.DATA_FUEL_LEVEL_SLOT) > 0;
    }

    /**
     * Calculates how much of the flame should be rendered.
     * @return The height of the flame in pixels.
     */
    public int fireAmount()
    {
        int fuelLevel = data.get(AlchemicalCrucibleBlockEntity.DATA_FUEL_LEVEL_SLOT);
        int maxFuel = data.get(AlchemicalCrucibleBlockEntity.DATA_MAX_FUEL_SLOT);
        int flameMaxSize = 14;
        float factor = (float) flameMaxSize / maxFuel;

        return (int)(fuelLevel * factor);
    }

    public int essenceSprite()
    {
        int spriteCount = 5;
        int progress = data.get(AlchemicalCrucibleBlockEntity.DATA_CRAFT_PROGRESS_SLOT);
        int maxProgress = data.get(AlchemicalCrucibleBlockEntity.DATA_MAX_CRAFT_PROGRESS_SLOT);
        boolean isEssence = data.get(AlchemicalCrucibleBlockEntity.DATA_IS_ESSENCE_SLOT) > 0;
        if (isEssence)
        {
            return spriteCount-1;
        }
        float percentageComplete = (float) progress /maxProgress;
        return (int)Math.floor(spriteCount * percentageComplete);
    }

    /**
     * If the block entity is in the process of crafting.
     * @return Whether the block entity is crafting.
     */
    public boolean isCrafting()
    {
        return data.get(AlchemicalCrucibleBlockEntity.DATA_CRAFT_PROGRESS_SLOT) > 0;
    }

    /**
     * Get the current progress of the progress arrow.
     * @return The size of the progress arrow in pixels.
     */
    public int getCraftingArrowProgress(int maxHeight)
    {
        int progress = data.get(AlchemicalCrucibleBlockEntity.DATA_CRAFT_PROGRESS_SLOT);
        int maxProgress = data.get(AlchemicalCrucibleBlockEntity.DATA_MAX_CRAFT_PROGRESS_SLOT);
        float factor = (float)maxHeight / maxProgress;

        return maxProgress != 0 && progress != 0 ? (int)(progress * factor) : 0;
    }

    /**
     * If the crucible is in the progress of transferring fluid to a bottle.
     * @return TRUE if a transfer is in progress. FALSE if a transfer is not in progress.
     */
    public boolean isTransferring()
    {
        return data.get(AlchemicalCrucibleBlockEntity.DATA_TRANSFER_PROGRESS_SLOT) > 0;
    }

    /**
     * Get the size of the transfer arrow.
     * @param maxSize The maximum size of the transfer arrow
     * @return The required size of the transfer arrow in pixels.
     */
    public int getTransferArrowSize(int maxSize)
    {
        int progress = data.get(AlchemicalCrucibleBlockEntity.DATA_TRANSFER_PROGRESS_SLOT);
        int maxProgress = data.get(AlchemicalCrucibleBlockEntity.DATA_MAX_TRANSFER_PROGRESS_SLOT);
        float percentageComplete = (float)progress / maxProgress;
        return (int)(maxSize * percentageComplete);
    }

    /**
     * Whether the menu should still be open.
     * @param player The player with the menu open.
     * @return Whether the menu should still be open.
     */
    @Override
    public boolean stillValid(Player player)
    {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.ALCHEMICAL_CRUCIBLE.get());
    }
}
