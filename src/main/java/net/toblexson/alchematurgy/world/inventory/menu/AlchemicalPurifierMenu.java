package net.toblexson.alchematurgy.world.inventory.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.toblexson.alchematurgy.registry.ModBlocks;
import net.toblexson.alchematurgy.registry.ModItems;
import net.toblexson.alchematurgy.registry.ModMenuTypes;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalPurifierBlockEntity;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalSeparatorBlockEntity;
import net.toblexson.alchematurgy.world.item.BottledEssenceItem;

import java.util.Objects;

public class AlchemicalPurifierMenu extends ModMenu
{
    /**
     * Client-side constructor.
     * @param containerId The id of the container.
     * @param inventory The player's inventory.
     * @param data Extra data from a buffer.
     */
    public AlchemicalPurifierMenu(int containerId, Inventory inventory, FriendlyByteBuf data)
    {
        this(containerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(data.readBlockPos())),
             new SimpleContainerData(AlchemicalPurifierBlockEntity.DATA_COUNT));
    }

    /**
     * Server-side constructor.
     * @param containerId The id of the container.
     * @param inventory The player's inventory.
     * @param blockEntity The menu's block entity.
     * @param data The container data.
     */
    public AlchemicalPurifierMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData data)
    {
        super(ModMenuTypes.ALCHEMICAL_PURIFIER.get(), containerId, inventory, blockEntity, data, 2);

        //input
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory,     AlchemicalPurifierBlockEntity.INPUT_SLOT,
                                            56, 35, (stack) ->
                                                    stack.getItem() instanceof BottledEssenceItem && ((BottledEssenceItem) stack.getItem()).isDirty()));

        //output
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory, AlchemicalPurifierBlockEntity.OUTPUT_SLOT,
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
     * Determines whether supplied player can use this container
     * @param player The player with the menu open.
     * @return Whether the menu should still be open.
     */
    @Override
    public boolean stillValid(Player player)
    {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.ALCHEMICAL_PURIFIER.get());
    }
}
