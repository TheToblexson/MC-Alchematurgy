package net.toblexson.alchematurgy.world.inventory.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.toblexson.alchematurgy.registry.ModBlocks;
import net.toblexson.alchematurgy.registry.ModItems;
import net.toblexson.alchematurgy.registry.ModMenuTypes;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalSeparatorBlockEntity;

import java.util.Objects;

public class AlchemicalSeparatorMenu extends AbstractContainerMenu
{
    private static final int PLAYER_HOTBAR_SIZE = 9;
    private static final int PLAYER_INVENTORY_ROWS = 3;
    private static final int PLAYER_INVENTORY_COLUMNS = 9;
    private static final int PLAYER_INVENTORY_SIZE = PLAYER_INVENTORY_COLUMNS * PLAYER_INVENTORY_ROWS;
    private static final int PLAYER_FULL_INVENTORY_SIZE = PLAYER_HOTBAR_SIZE + PLAYER_INVENTORY_SIZE;
    private static final int PLAYER_FULL_INVENTORY_START = 0;

    private static final int INVENTORY_START = PLAYER_FULL_INVENTORY_START + PLAYER_FULL_INVENTORY_SIZE;
    private static final int INVENTORY_SIZE = 2;

    private final AlchemicalSeparatorBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    /**
     * Client-side constructor.
     * @param containerId The id of the container.
     * @param inventory The player's inventory.
     * @param data Extra data from a buffer.
     */
    public AlchemicalSeparatorMenu(int containerId, Inventory inventory, FriendlyByteBuf data)
    {
        this(containerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(data.readBlockPos())),
             new SimpleContainerData(AlchemicalSeparatorBlockEntity.DATA_COUNT));
    }

    /**
     * Server-side constructor.
     * @param containerId The id of the container.
     * @param inventory The player's inventory.
     * @param blockEntity The block entity that the menu is attached to.
     */
    public AlchemicalSeparatorMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData data)
    {
        super(ModMenuTypes.ALCHEMICAL_SEPARATOR.get(), containerId);
        this.blockEntity = (AlchemicalSeparatorBlockEntity) blockEntity;
        this.level = inventory.player.level();
        this.data = data;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        addDataSlots(data);

        //input
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory, AlchemicalSeparatorBlockEntity.INPUT_SLOT,
                                         56, 35, (stack) -> stack.is(ModItems.BOTTLED_MIXED_ESSENCE.get())));

        //output
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory, AlchemicalSeparatorBlockEntity.OUTPUT_SLOT,
                                         116, 35, (stack) -> false));

    }

    /**
     * If the block entity is in the process of crafting.
     * @return Whether the block entity is crafting.
     */
    public boolean isCrafting()
    {
        return data.get(AlchemicalSeparatorBlockEntity.DATA_PROGRESS_SLOT) > 0;
    }

    /**
     * Get the current progress of the progress arrow.
     * @return The size of the progress arrow in pixels.
     */
    public int getCraftingArrowProgress()
    {
        int progress = data.get(AlchemicalSeparatorBlockEntity.DATA_PROGRESS_SLOT);
        int maxProgress = data.get(AlchemicalSeparatorBlockEntity.DATA_MAX_PROGRESS_SLOT);
        int arrowMaxSize = 22;
        float factor = (float) arrowMaxSize / maxProgress;

        return maxProgress != 0 && progress != 0 ? (int)(progress * factor) : 0;
    }

    /**
     * Move a stack from a shift click action.
     * CREDIT GOES TO: diesieben07 | <a href="https://github.com/diesieben07/SevenCommons">...</a>
     * constants renamed for personal preference.
     * @param player The player interacting with the menu.
     * @param index The index of the slot clicked.
     * @return The resulting item stack.
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
            if (!moveItemStackTo(stack, INVENTORY_START, INVENTORY_START + INVENTORY_SIZE - 1, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < INVENTORY_START + INVENTORY_SIZE) {
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
     * Whether the menu should still be open.
     * @param player The player with the menu open.
     * @return Whether the menu should still be open.
     */
    @Override
    public boolean stillValid(Player player)
    {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.ALCHEMICAL_SEPARATOR.get());
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
