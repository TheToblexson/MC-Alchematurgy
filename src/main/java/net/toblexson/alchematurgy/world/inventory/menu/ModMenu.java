package net.toblexson.alchematurgy.world.inventory.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.toblexson.alchematurgy.registry.ModMenuTypes;
import net.toblexson.alchematurgy.world.block.entity.ModMenuBlockEntity;

public abstract class ModMenu extends AbstractContainerMenu
{
    private static final int PLAYER_HOTBAR_SIZE = 9;
    private static final int PLAYER_INVENTORY_ROWS = 3;
    private static final int PLAYER_INVENTORY_COLUMNS = 9;
    private static final int PLAYER_INVENTORY_SIZE = PLAYER_INVENTORY_COLUMNS * PLAYER_INVENTORY_ROWS;
    private static final int PLAYER_FULL_INVENTORY_SIZE = PLAYER_HOTBAR_SIZE + PLAYER_INVENTORY_SIZE;
    private static final int PLAYER_FULL_INVENTORY_START = 0;

    private static final int INVENTORY_START = PLAYER_FULL_INVENTORY_START + PLAYER_FULL_INVENTORY_SIZE;

    private final int inventorySize;
    protected final ModMenuBlockEntity blockEntity;
    protected final Level level;
    protected final ContainerData data;

    /**
     * Server-side constructor.
     * @param type The registered menu type.
     * @param containerId The id of the container.
     * @param inventory The player's inventory.
     * @param blockEntity The menu's block entity.
     * @param data The container data.
     * @param inventorySize The size of the container's inventory.
     */
    public ModMenu(MenuType<?> type, int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData data, int inventorySize)
    {
        super(type, containerId);
        this.inventorySize = inventorySize;
        this.blockEntity = (ModMenuBlockEntity) blockEntity;
        this.level = inventory.player.level();
        this.data = data;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        addDataSlots(data);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player inventory and the other inventory(s).
     * CREDIT GOES TO: diesieben07 | <a href="https://github.com/diesieben07/SevenCommons">...</a>
     * constants renamed for personal preference.
     * @param player The player interacting with the menu.
     * @param index The index of the slot clicked.
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        Slot source = slots.get(index);
        if (!source.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack stack = source.getItem();
        ItemStack copy = stack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < PLAYER_FULL_INVENTORY_START + PLAYER_FULL_INVENTORY_SIZE) {
            // This is a vanilla container slot so merge the stack into the tile inventory (-1 to avoid the result slot)
            if (!moveItemStackTo(stack, INVENTORY_START, INVENTORY_START + inventorySize - 1, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < INVENTORY_START + inventorySize) {
            // This is a BE slot so merge the stack into the players inventory
            if (!moveItemStackTo(stack, PLAYER_FULL_INVENTORY_START, PLAYER_FULL_INVENTORY_START + PLAYER_FULL_INVENTORY_SIZE, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (stack.getCount() == 0) {
            source.set(ItemStack.EMPTY);
        } else {
            source.setChanged();
        }
        source.onTake(player, stack);
        return copy;
    }

    /**
     * Add the player's main inventory to the menu.
     * @param inventory The player's inventory.
     */
    private void addPlayerInventory(Inventory inventory)
    {
        for (int y = 0; y < PLAYER_INVENTORY_ROWS; ++y)
            for (int x = 0; x < PLAYER_INVENTORY_COLUMNS; ++x)
                addSlot(new Slot(inventory, x + y * PLAYER_INVENTORY_COLUMNS + PLAYER_HOTBAR_SIZE, 8 + x * 18, 84 + y * 18));
    }

    /**
     * Add the player's hotbar to the menu.
     * @param inventory The player's inventory.
     */
    private void addPlayerHotbar(Inventory inventory)
    {
        for (int x = 0; x < 9; ++x)
            addSlot(new Slot(inventory, x,  8 + x * 18, 142));
    }
}
