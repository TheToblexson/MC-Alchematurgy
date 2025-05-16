package net.toblexson.alchematurgy.world.inventory.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
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
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory,AlchemicalCrucibleBlockEntity.INPUT_SLOT, 56, 17));
        //fuel
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory,AlchemicalCrucibleBlockEntity.FUEL_SLOT,
                                            56, 53,  (stack) -> stack.getBurnTime(null) > 0));
        //bottle
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory,AlchemicalCrucibleBlockEntity.BOTTLE_SLOT,
                                            84, 17, (stack -> stack.is(Items.GLASS_BOTTLE))));
        //output
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory,AlchemicalCrucibleBlockEntity.OUTPUT_SLOT,
                                         116, 35, (stack) -> false));

        addDataSlots(data);
    }

    /**
     * If there is water in the block entity.
     * @return True if the water level is above 0.
     */
    public boolean hasWater()
    {
        return data.get(AlchemicalCrucibleBlockEntity.DATA_WATER_AMOUNT_SLOT) > 0;
    }

    /**
     * Calculates the amount of the water cover texture to render.
     * @return the height the water cover texture should be.
     */
    public int waterAmount()
    {
        return data.get(AlchemicalCrucibleBlockEntity.DATA_WATER_AMOUNT_SLOT);
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

    /**
     * If the block entity is in the process of crafting.
     * @return Whether the block entity is crafting.
     */
    public boolean isCrafting()
    {
        return data.get(AlchemicalCrucibleBlockEntity.DATA_PROGRESS_SLOT) > 0;
    }

    /**
     * Get the current progress of the progress arrow.
     * @return The size of the progress arrow in pixels.
     */
    public int getCraftingArrowProgress()
    {
        int progress = data.get(AlchemicalCrucibleBlockEntity.DATA_PROGRESS_SLOT);
        int maxProgress = data.get(AlchemicalCrucibleBlockEntity.DATA_MAX_PROGRESS_SLOT);
        int arrowMaxSize = 28;
        float factor = (float) arrowMaxSize / maxProgress;

        return maxProgress != 0 && progress != 0 ? (int)(progress * factor) : 0;
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
