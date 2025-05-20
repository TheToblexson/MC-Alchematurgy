package net.toblexson.alchematurgy.world.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.toblexson.alchematurgy.registry.*;
import net.toblexson.alchematurgy.world.inventory.menu.AlchemicalFabricatorMenu;
import net.toblexson.alchematurgy.world.item.BottledEssenceItem;

public class AlchemicalFabricatorBlockEntity extends ModMenuBlockEntity
{
    public static final int INVENTORY_SIZE = 8;
    public static final int AIR_SLOT = 0;
    public static final int EARTH_SLOT = 1;
    public static final int FIRE_SLOT = 2;
    public static final int WATER_SLOT = 3;
    public static final int LIFE_SLOT = 4;
    public static final int MAGIC_SLOT = 5;
    public static final int TARGET_SLOT = 6;
    public static final int OUTPUT_SLOT = 7;

    public static final int DATA_COUNT = 12;
    public static final int DATA_AIR_COUNT_SLOT = 0;
    public static final int DATA_EARTH_COUNT_SLOT = 1;
    public static final int DATA_FIRE_COUNT_SLOT = 2;
    public static final int DATA_WATER_COUNT_SLOT = 3;
    public static final int DATA_LIFE_COUNT_SLOT = 4;
    public static final int DATA_MAGIC_COUNT_SLOT = 5;
    public static final int DATA_AIR_REQUIRED_SLOT = 6;
    public static final int DATA_EARTH_REQUIRED_SLOT = 7;
    public static final int DATA_FIRE_REQUIRED_SLOT = 8;
    public static final int DATA_WATER_REQUIRED_SLOT = 9;
    public static final int DATA_LIFE_REQUIRED_SLOT = 10;
    public static final int DATA_MAGIC_REQUIRED_SLOT = 11;

    private int air_count = 0;
    private int earth_count = 0;
    private int fire_count = 0;
    private int water_count = 0;
    private int life_count = 0;
    private int magic_count = 0;
    private int air_required = 0;
    private int earth_required = 0;
    private int fire_required = 0;
    private int water_required = 0;
    private int life_required = 0;
    private int magic_required = 0;

    public AlchemicalFabricatorBlockEntity(BlockPos pos, BlockState state)
    {
        super(ModBlockEntityTypes.ALCHEMICAL_FABRICATOR.get(), pos, state, INVENTORY_SIZE);
    }

    /**
     * Initialise the container data for the container.
     * @return the initialised container data.
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
                    case DATA_AIR_COUNT_SLOT -> AlchemicalFabricatorBlockEntity.this.air_count;
                    case DATA_EARTH_COUNT_SLOT -> AlchemicalFabricatorBlockEntity.this.earth_count;
                    case DATA_FIRE_COUNT_SLOT -> AlchemicalFabricatorBlockEntity.this.fire_count;
                    case DATA_WATER_COUNT_SLOT -> AlchemicalFabricatorBlockEntity.this.water_count;
                    case DATA_LIFE_COUNT_SLOT -> AlchemicalFabricatorBlockEntity.this.life_count;
                    case DATA_MAGIC_COUNT_SLOT -> AlchemicalFabricatorBlockEntity.this.magic_count;
                    case DATA_AIR_REQUIRED_SLOT -> AlchemicalFabricatorBlockEntity.this.air_required;
                    case DATA_EARTH_REQUIRED_SLOT -> AlchemicalFabricatorBlockEntity.this.earth_required;
                    case DATA_FIRE_REQUIRED_SLOT -> AlchemicalFabricatorBlockEntity.this.fire_required;
                    case DATA_WATER_REQUIRED_SLOT -> AlchemicalFabricatorBlockEntity.this.water_required;
                    case DATA_LIFE_REQUIRED_SLOT -> AlchemicalFabricatorBlockEntity.this.life_required;
                    case DATA_MAGIC_REQUIRED_SLOT -> AlchemicalFabricatorBlockEntity.this.magic_required;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value)
            {
                switch (index)
                {

                    case DATA_AIR_COUNT_SLOT: AlchemicalFabricatorBlockEntity.this.air_count = value;
                    case DATA_EARTH_COUNT_SLOT: AlchemicalFabricatorBlockEntity.this.earth_count = value;
                    case DATA_FIRE_COUNT_SLOT: AlchemicalFabricatorBlockEntity.this.fire_count = value;
                    case DATA_WATER_COUNT_SLOT: AlchemicalFabricatorBlockEntity.this.water_count = value;
                    case DATA_LIFE_COUNT_SLOT: AlchemicalFabricatorBlockEntity.this.life_count = value;
                    case DATA_MAGIC_COUNT_SLOT: AlchemicalFabricatorBlockEntity.this.magic_count = value;
                    case DATA_AIR_REQUIRED_SLOT: AlchemicalFabricatorBlockEntity.this.air_required = value;
                    case DATA_EARTH_REQUIRED_SLOT: AlchemicalFabricatorBlockEntity.this.earth_required = value;
                    case DATA_FIRE_REQUIRED_SLOT: AlchemicalFabricatorBlockEntity.this.fire_required = value;
                    case DATA_WATER_REQUIRED_SLOT: AlchemicalFabricatorBlockEntity.this.water_required = value;
                    case DATA_LIFE_REQUIRED_SLOT: AlchemicalFabricatorBlockEntity.this.life_required = value;
                    case DATA_MAGIC_REQUIRED_SLOT: AlchemicalFabricatorBlockEntity.this.magic_required = value;
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
     * The ticking method for the block entity.
     * @param level The current level.
     * @param pos The block position.
     * @param state The block state.
     */
    public void tick(Level level, BlockPos pos, BlockState state)
    {
        ItemStack target = inventory.getStackInSlot(TARGET_SLOT);
        if (!target.isEmpty())
        {
            ModDataMaps.Essences essences = target.getItemHolder().getData(ModDataMaps.ESSENCES);
            if (essences != null)
            {
                updateRequiredEssences(essences);
                ItemStack output = new ItemStack(target.getItem());
                if (canOutput(output))
                {
                    //if progress has reached the required amount, craft
                    if (hasAllEssences())
                    {
                        finishCraft(output);
                        resetEssences();
                    }
                    consumeEssences();
                }
            } else
            {
                disableCounters();
            }
        }
        else
            disableCounters();
    }

    private void disableCounters()
    {
        air_required = 0;
        earth_required = 0;
        fire_required = 0;
        water_required = 0;
        life_required = 0;
        magic_required = 0;
    }

    private void consumeEssences()
    {
        if (air_count < air_required && !inventory.getStackInSlot(AIR_SLOT).isEmpty())
        {
            BottledEssenceItem item = (BottledEssenceItem)inventory.extractItem(AIR_SLOT, 1, false).getItem();
            air_count += item.getEssenceAmount();
        }
        if (earth_count < earth_required && !inventory.getStackInSlot(EARTH_SLOT).isEmpty())
        {
            BottledEssenceItem item = (BottledEssenceItem)inventory.extractItem(EARTH_SLOT, 1, false).getItem();
            earth_count += item.getEssenceAmount();
        }
        if (fire_count < fire_required && !inventory.getStackInSlot(FIRE_SLOT).isEmpty())
        {
            BottledEssenceItem item = (BottledEssenceItem)inventory.extractItem(FIRE_SLOT, 1, false).getItem();
            fire_count += item.getEssenceAmount();
        }
        if (water_count < water_required && !inventory.getStackInSlot(WATER_SLOT).isEmpty())
        {
            BottledEssenceItem item = (BottledEssenceItem)inventory.extractItem(WATER_SLOT, 1, false).getItem();
            water_count += item.getEssenceAmount();
        }
        if (life_count < life_required && !inventory.getStackInSlot(LIFE_SLOT).isEmpty())
        {
            BottledEssenceItem item = (BottledEssenceItem)inventory.extractItem(LIFE_SLOT, 1, false).getItem();
            life_count += item.getEssenceAmount();
        }
        if (magic_count < magic_required && !inventory.getStackInSlot(MAGIC_SLOT).isEmpty())
        {
            BottledEssenceItem item = (BottledEssenceItem)inventory.extractItem(MAGIC_SLOT, 1, false).getItem();
            magic_count += item.getEssenceAmount();
        }
    }

    private void finishCraft(ItemStack output)
    {
        int newCount = inventory.getStackInSlot(OUTPUT_SLOT).getCount() + output.getCount();
        output.setCount(newCount);
        resetEssences();
        inventory.setStackInSlot(OUTPUT_SLOT,output);
    }

    private void resetEssences()
    {
        air_count = 0;
        earth_count = 0;
        fire_count = 0;
        water_count = 0;
        life_count = 0;
        magic_count = 0;
    }

    private boolean hasAllEssences()
    {
        return air_count >= air_required &&
               earth_count >= earth_required &&
               fire_count >= fire_required &&
               water_count >= water_required &&
               life_count >= life_required &&
               magic_count >= magic_required;
    }

    private void updateRequiredEssences(ModDataMaps.Essences essences)
    {
        air_required = (int)Math.ceil(essences.air());
        earth_required = (int)Math.ceil(essences.earth());
        fire_required = (int)Math.ceil(essences.fire());
        water_required = (int)Math.ceil(essences.water());
        life_required = (int)Math.ceil(essences.life());
        magic_required = (int)Math.ceil(essences.magic());

    }

    private boolean canOutput(ItemStack output)
    {
        ItemStack stack = inventory.getStackInSlot(OUTPUT_SLOT);
        if (stack.isEmpty())
            return true;
        int maxCount = stack.getMaxStackSize();
        int outputCount = stack.getCount() + 1;
        return stack.getItem() == output.getItem() && outputCount <= maxCount;
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
        tag.putInt("air_count", air_count);
        tag.putInt("earth_count", earth_count);
        tag.putInt("fire_count", fire_count);
        tag.putInt("water_count", water_count);
        tag.putInt("life_count", life_count);
        tag.putInt("magic_count", magic_count);
        tag.putInt("air_required", air_required);
        tag.putInt("earth_required", earth_required);
        tag.putInt("fire_required", fire_required);
        tag.putInt("water_required", water_required);
        tag.putInt("life_required", life_required);
        tag.putInt("magic_required", magic_required);
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
        air_count = tag.getInt("air_count");
        earth_count = tag.getInt("earth_count");
        fire_count = tag.getInt("fire_count");
        water_count = tag.getInt("water_count");
        life_count = tag.getInt("life_count");
        magic_count = tag.getInt("magic_count");
        air_required = tag.getInt("air_required");
        earth_required = tag.getInt("earth_required");
        fire_required = tag.getInt("fire_required");
        water_required = tag.getInt("water_required");
        life_required = tag.getInt("life_required");
        magic_required = tag.getInt("magic_required");
    }

    /**
     * Get the inventory's display name.
     * @return The display name.
     */
    @Override
    public Component getDisplayName()
    {
        return ModBlocks.ALCHEMICAL_FABRICATOR.get().getName();
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
        return new AlchemicalFabricatorMenu(containerID, inventory, this, data);
    }
}
