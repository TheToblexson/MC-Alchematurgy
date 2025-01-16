package net.toblexson.alchematurgy.world.inventory.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.toblexson.alchematurgy.registry.ModBlocks;
import net.toblexson.alchematurgy.registry.ModMenuTypes;

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
        this(containerID, playerInventory, ContainerLevelAccess.NULL);
    }

    /**
     * The extended constructor.
     * @param containerID The container ID.
     * @param playerInventory The player inventory.
     * @param access The level access.
     */
    public AlchemicalCrucibleMenu(int containerID, Inventory playerInventory, ContainerLevelAccess access)
    {
        super(ModMenuTypes.ALCHEMICAL_CRUCIBLE_MENU.get(), containerID);
        this.access = access;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex)
    {
        return null;
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
