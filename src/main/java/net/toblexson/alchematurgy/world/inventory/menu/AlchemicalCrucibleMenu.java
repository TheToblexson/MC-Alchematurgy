package net.toblexson.alchematurgy.world.inventory.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.toblexson.alchematurgy.registry.ModBlocks;
import net.toblexson.alchematurgy.registry.ModMenuTypes;

import static net.toblexson.alchematurgy.world.block.entity.AlchemicalCrucibleBlockEntity.*;

/**
 * The menu for the alchemical crucible.
 */
public class AlchemicalCrucibleMenu extends AbstractContainerMenu
{
    private final ContainerLevelAccess access;

    /**
     * The minimal crucible menu constructor for registration and client-side use.
     * @param containerID The container ID.
     * @param playerInventory The player inventory.
     */
    public AlchemicalCrucibleMenu(int containerID, Inventory playerInventory)
    {
        this(containerID, playerInventory, ContainerLevelAccess.NULL, new ItemStackHandler(INVENTORY_SIZE));
    }

    /**
     * The extended constructor.
     * @param containerID The container ID.
     * @param playerInventory The player inventory.
     * @param access The level access.
     */
    public AlchemicalCrucibleMenu(int containerID, Inventory playerInventory, ContainerLevelAccess access, IItemHandler dataInventory)
    {
        super(ModMenuTypes.ALCHEMICAL_CRUCIBLE_MENU.get(), containerID);
        if (dataInventory.getSlots() == INVENTORY_SIZE)
        {
            addSlot(new SlotItemHandler(dataInventory, INPUT_SLOT, 56, 17));
            addSlot(new SlotItemHandler(dataInventory,FUEL_SLOT,56,53));
            addSlot(new SlotItemHandler(dataInventory,OUTPUT_SLOT,116,35));
        }

        //player_inventory
        int startX = 8;
        int startY = 84;
        int i = 9;
        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 9; x++)
            {
                addSlot(new Slot(playerInventory, i, startX + (18 * x), startY + (18 * y)));
                i++;
            }
        }

        //hotbar
        //startX = 8;
        startY = 142;
        for (int x = 0; x < 9; x++)
        {
            addSlot(new Slot(playerInventory, x, startX + (18 * x), startY));
        }

        this.access = access;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        // The quick moved slot stack
        ItemStack movedStack = ItemStack.EMPTY;
        // The quick moved slot
        Slot movedSlot = this.slots.get(index);
        // If the the slot is not empty
        if (movedSlot.hasItem())
        {
            // Get the raw stack to move
            ItemStack rawStack = movedSlot.getItem();
            // Set the slot stack to a copy of the raw stack
            movedStack = rawStack.copy();
            // If the quick move was performed on the inventory slots
            if (index < INVENTORY_SIZE)
            {
                // Try to move the result slot into the player inventory/hotbar
                if (!this.moveItemStackTo(rawStack, INVENTORY_SIZE, INVENTORY_SIZE + 9 + 27, false)) {
                    // If cannot move, no longer quick move
                    return ItemStack.EMPTY;
                }
                // Perform logic on result slot quick move
                movedSlot.onQuickCraft(rawStack, movedStack);
            }
            // Else the quick move was performed on the player inventory or hotbar slot
            else
            {
                // Try to move the inventory/hotbar slot into the inventory slots (ignoring the output)
                if (!this.moveItemStackTo(rawStack, 0, INVENTORY_SIZE - 1, false)) {
                    // If cannot move and in player inventory slot, try to move to hotbar
                    if (index < INVENTORY_SIZE + 27)
                    {
                        if (!this.moveItemStackTo(rawStack, INVENTORY_SIZE + 27, INVENTORY_SIZE + 9 + 27, false)) {
                            // If cannot move, no longer quick move
                            return ItemStack.EMPTY;
                        }
                    }
                    // Else try to move hotbar into player inventory slot
                    else if (!this.moveItemStackTo(rawStack, INVENTORY_SIZE, INVENTORY_SIZE + 27, false)) {
                        // If cannot move, no longer quick move
                        return ItemStack.EMPTY;
                    }
                }
            }
            if (rawStack.isEmpty())
            {
                // If the raw stack has completely moved out of the slot, set the slot to the empty stack
                movedSlot.set(ItemStack.EMPTY);
            } else
            {
                // Otherwise, notify the slot that that the stack count has changed
                movedSlot.setChanged();
            }
            if (rawStack.getCount() == movedStack.getCount())
            {
                // If the raw stack was not able to be moved to another slot, no longer quick move
                return ItemStack.EMPTY;
            }
            // Execute logic on what to do post move with the remaining stack
            movedSlot.onTake(player, rawStack);
        }
        return movedStack; // Return the slot stack
    }

    /**
     * Determines whether the menu should stay open.
     * @param player The player.
     * @return Whether the menu should stay open.
     */
    @Override
    public boolean stillValid(Player player)
    {
        return AbstractContainerMenu.stillValid(access, player, ModBlocks.ALCHEMICAL_CRUCIBLE.get());
    }
}
