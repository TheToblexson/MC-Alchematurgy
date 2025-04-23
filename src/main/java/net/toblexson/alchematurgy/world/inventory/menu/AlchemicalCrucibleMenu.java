package net.toblexson.alchematurgy.world.inventory.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.toblexson.alchematurgy.registry.ModBlocks;
import net.toblexson.alchematurgy.registry.ModMenuTypes;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalCrucibleBlockEntity;

import java.util.Objects;

public class AlchemicalCrucibleMenu extends AbstractContainerMenu
{
    private static final int PLAYER_HOTBAR_SIZE = 9;
    private static final int PLAYER_INVENTORY_ROWS = 3;
    private static final int PLAYER_INVENTORY_COLUMNS = 9;
    private static final int PLAYER_INVENTORY_SIZE = PLAYER_INVENTORY_COLUMNS * PLAYER_INVENTORY_ROWS;
    private static final int PLAYER_FULL_INVENTORY_SIZE = PLAYER_HOTBAR_SIZE + PLAYER_INVENTORY_SIZE;
    private static final int PLAYER_FULL_INVENTORY_START = 0;

    private static final int INVENTORY_START = PLAYER_FULL_INVENTORY_START + PLAYER_FULL_INVENTORY_SIZE;
    private static final int INVENTORY_SIZE = 4;

    /**
     * The block entity this menu is attached to.
     */
    public final AlchemicalCrucibleBlockEntity blockEntity;
    /**
     * The level that the menu is open in.
     */
    private final Level level;

    /**
     * Create the menu for the Alchemical Crucible
     * @param containerId The id of the container.
     * @param inventory The player's inventory.
     * @param extra Extra data from a buffer.
     */
    public AlchemicalCrucibleMenu(int containerId, Inventory inventory, FriendlyByteBuf extra)
    {
        this(containerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(extra.readBlockPos())));
    }

    /**
     * Create the menu for the Alchemical Crucible.
     * @param containerId The id of the container.
     * @param inventory The player's inventory.
     * @param blockEntity The block entity that the menu is attached to.
     */
    public AlchemicalCrucibleMenu(int containerId, Inventory inventory, BlockEntity blockEntity)
    {
        super(ModMenuTypes.ALCHEMICAL_CRUCIBLE_MENU.get(), containerId);
        this.blockEntity = (AlchemicalCrucibleBlockEntity) blockEntity;
        this.level = inventory.player.level();

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        //Need custom slots

        //input
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory,0, 56, 17)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                //TODO Only allow insertion if a recipe exists.
                return super.mayPlace(stack);
            }
        });
        //fuel
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory,1, 56, 53)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return stack.getBurnTime(null) > 0;
            }
        });
        //bottle
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory,2, 84, 17)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return stack.is(Items.GLASS_BOTTLE);
            }
        });
        //output
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory,3, 116, 35)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return false;
            }
        });
    }

    /**
     * Move a stack from a shift click action.
     * CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
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
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(stack, INVENTORY_START, INVENTORY_START + INVENTORY_SIZE, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < INVENTORY_START + INVENTORY_SIZE) {
            // This is a TE slot so merge the stack into the players inventory
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
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.ALCHEMICAL_CRUCIBLE.get());
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
