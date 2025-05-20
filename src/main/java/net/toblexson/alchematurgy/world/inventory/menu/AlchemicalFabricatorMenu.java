package net.toblexson.alchematurgy.world.inventory.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.toblexson.alchematurgy.Essence;
import net.toblexson.alchematurgy.registry.ModBlocks;
import net.toblexson.alchematurgy.registry.ModDataMaps;
import net.toblexson.alchematurgy.registry.ModMenuTypes;
import net.toblexson.alchematurgy.world.block.entity.AlchemicalFabricatorBlockEntity;
import net.toblexson.alchematurgy.world.item.BottledEssenceItem;

import java.util.Objects;

public class AlchemicalFabricatorMenu extends ModMenu
{
    /**
     * Client-side constructor.
     * @param containerId The id of the container.
     * @param inventory The player's inventory.
     * @param data Extra data from a buffer.
     */
    public AlchemicalFabricatorMenu(int containerId, Inventory inventory, FriendlyByteBuf data)
    {
        this(containerId, inventory, Objects.requireNonNull(inventory.player.level().getBlockEntity(data.readBlockPos())),
             new SimpleContainerData(AlchemicalFabricatorBlockEntity.DATA_COUNT));
    }

    /**
     * Server-side constructor.
     * @param containerId The id of the container.
     * @param inventory The player's inventory.
     * @param blockEntity The block entity that the menu is attached to.
     */
    public AlchemicalFabricatorMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData data)
    {
        super(ModMenuTypes.ALCHEMICAL_FABRICATOR.get(), containerId, inventory, blockEntity, data, AlchemicalFabricatorBlockEntity.INVENTORY_SIZE);

        int start = 17;
        int step = 18;

        //air
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory, AlchemicalFabricatorBlockEntity.AIR_SLOT,
            8, start, (stack) -> stack.getItem() instanceof BottledEssenceItem bottle && bottle.getEssence() == Essence.Air));
        //earth
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory, AlchemicalFabricatorBlockEntity.EARTH_SLOT,
            8, start + step, (stack) -> stack.getItem() instanceof BottledEssenceItem bottle && bottle.getEssence() == Essence.Earth));
        //fire
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory, AlchemicalFabricatorBlockEntity.FIRE_SLOT,
            8, start + step * 2, (stack) -> stack.getItem() instanceof BottledEssenceItem bottle && bottle.getEssence() == Essence.Fire));
        //water
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory, AlchemicalFabricatorBlockEntity.WATER_SLOT,
            8, start + step * 3, (stack) -> stack.getItem() instanceof BottledEssenceItem bottle && bottle.getEssence() == Essence.Water));
        //life
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory, AlchemicalFabricatorBlockEntity.LIFE_SLOT,
            8, start + step * 4, (stack) -> stack.getItem() instanceof BottledEssenceItem bottle && bottle.getEssence() == Essence.Life));
        //magic
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory, AlchemicalFabricatorBlockEntity.MAGIC_SLOT,
            8, start + step * 5, (stack) -> stack.getItem() instanceof BottledEssenceItem bottle && bottle.getEssence() == Essence.Magic));

        //target
        this.addSlot(new SlotItemHandler(this.blockEntity.inventory, AlchemicalFabricatorBlockEntity.TARGET_SLOT,
            56, 62));

        //output
        this.addSlot(new ModSlotItemHandler(this.blockEntity.inventory, AlchemicalFabricatorBlockEntity.OUTPUT_SLOT,
            116, 62, (stack) -> false));

        addDataSlots(data);
    }

    @Override
    protected int getPlayerInventoryY()
    {
        return 138;
    }

    /**
     * Whether the menu should still be open.
     * @param player The player with the menu open.
     * @return Whether the menu should still be open.
     */
    @Override
    public boolean stillValid(Player player)
    {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.ALCHEMICAL_FABRICATOR.get());
    }

    public ModDataMaps.Essences getRequiredEssences()
    {
        return new ModDataMaps.Essences(data.get(AlchemicalFabricatorBlockEntity.DATA_AIR_REQUIRED_SLOT),
                                        data.get(AlchemicalFabricatorBlockEntity.DATA_EARTH_REQUIRED_SLOT),
                                        data.get(AlchemicalFabricatorBlockEntity.DATA_FIRE_REQUIRED_SLOT),
                                        data.get(AlchemicalFabricatorBlockEntity.DATA_WATER_REQUIRED_SLOT),
                                        data.get(AlchemicalFabricatorBlockEntity.DATA_LIFE_REQUIRED_SLOT),
                                        data.get(AlchemicalFabricatorBlockEntity.DATA_MAGIC_REQUIRED_SLOT));
    }

    public int getEssenceBarProgress(int essenceIndex)
    {
        //Data Essence slots are the same as the index and 6 + the index
        if (essenceIndex >= 6) return 0;
        int currentCount = data.get(essenceIndex);
        int maxCount = data.get(6 + essenceIndex);
        int barSize = 22;
        float factor = (float) barSize / maxCount;

        return (int)(currentCount * factor);
    }

    public ModDataMaps.Essences getEssenceCounts()
    {
        return new ModDataMaps.Essences(data.get(AlchemicalFabricatorBlockEntity.DATA_AIR_COUNT_SLOT),
                                        data.get(AlchemicalFabricatorBlockEntity.DATA_EARTH_COUNT_SLOT),
                                        data.get(AlchemicalFabricatorBlockEntity.DATA_FIRE_COUNT_SLOT),
                                        data.get(AlchemicalFabricatorBlockEntity.DATA_WATER_COUNT_SLOT),
                                        data.get(AlchemicalFabricatorBlockEntity.DATA_LIFE_COUNT_SLOT),
                                        data.get(AlchemicalFabricatorBlockEntity.DATA_MAGIC_COUNT_SLOT));
    }
}
